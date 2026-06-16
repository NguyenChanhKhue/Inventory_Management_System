package com.Khue.InventoryMgtSystem.services.Impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.dto.UserDTO;
import com.Khue.InventoryMgtSystem.dto.Auth.ForgotPasswordRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.LoginRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.RegisterRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.ResetPasswordRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.VerifyOtpRequest;
import com.Khue.InventoryMgtSystem.enums.UserRole;
import com.Khue.InventoryMgtSystem.exceptions.InvalidCredentialsException;
import com.Khue.InventoryMgtSystem.exceptions.NotFoundException;
import com.Khue.InventoryMgtSystem.models.PasswordResetToken;
import com.Khue.InventoryMgtSystem.models.User;
import com.Khue.InventoryMgtSystem.repository.PasswordResetTokenRepository;
import com.Khue.InventoryMgtSystem.repository.UserRepository;
import com.Khue.InventoryMgtSystem.security.JwtUtils;
import com.Khue.InventoryMgtSystem.services.EmailService;
import com.Khue.InventoryMgtSystem.services.UserService;
import com.Khue.InventoryMgtSystem.services.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private static final int OTP_EXPIRE_MINUTES = 5;
  private static final int RESET_TOKEN_EXPIRE_MINUTES = 10;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;
  private final JwtUtils jwtUtils;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final EmailService emailService;
  private final AuditLogService auditLogService;

  @Override
  public Response registerUser(RegisterRequest registerRequest) {
    UserRole role = UserRole.MANAGER;

    if (registerRequest.getRole() != null) {
      role = registerRequest.getRole();
    }

    User userToSave = User.builder()
        .name(registerRequest.getName())
        .email(registerRequest.getEmail())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .phoneNumber(registerRequest.getPhoneNumber())
        .role(role)
        .build();

    userRepository.save(userToSave);

    auditLogService.logAction("CREATE", "User", userToSave.getId(), "Tạo mới tài khoản: " + userToSave.getEmail());

    return Response.builder()
        .status(200)
        .message("User was successfully registed")
        .build();
  }

  @Override
  public Response loginUser(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new NotFoundException("Email Not Found"));

    if (!user.isActive()) {
      throw new InvalidCredentialsException("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin.");
    }

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Email hoặc mật khẩu không chính xác");
    }

    String token = jwtUtils.generateToken(user.getEmail());

    return Response.builder()
        .status(200)
        .message("User Logged in Successfully")
        .role(user.getRole().name())
        .token(token)
        .expirationTime("6 month")
        .build();
  }

  @Override
  @Transactional
  public Response forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
    User user = userRepository.findByEmail(forgotPasswordRequest.getEmail()).orElse(null);

    if (user == null) {
      return Response.builder()
          .status(200)
          .message("Email does not exist")
          .emailExists(false)
          .build();
    }

    expireActiveResetTokens(user.getId());

    String otp = generateOtp();
    PasswordResetToken passwordResetToken = PasswordResetToken.builder()
        .tokenHash(hashValue(otp))
        .user(user)
        .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES))
        .otpVerified(false)
        .used(false)
        .createdAt(LocalDateTime.now())
        .build();

    passwordResetTokenRepository.save(passwordResetToken);
    emailService.sendPasswordResetOtp(user.getEmail(), user.getName(), otp);

    return Response.builder()
        .status(200)
        .message("OTP has been sent to your email")
        .emailExists(true)
        .build();
  }

  @Override
  @Transactional
  public Response verifyPasswordResetOtp(VerifyOtpRequest verifyOtpRequest) {
    User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
        .orElseThrow(() -> new NotFoundException("Email Not Found"));

    PasswordResetToken passwordResetToken = passwordResetTokenRepository
        .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("No active OTP request found"));

    if (passwordResetToken.isOtpVerified()) {
      throw new IllegalArgumentException("Mã OTP này đã được sử dụng");
    }

    if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Mã OTP đã hết hạn");
    }

    if (!passwordResetToken.getTokenHash().equals(hashValue(verifyOtpRequest.getOtp()))) {
      throw new IllegalArgumentException("Mã OTP không chính xác");
    }

    String resetToken = generateResetToken();
    passwordResetToken.setTokenHash(hashValue(resetToken));
    passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRE_MINUTES));
    passwordResetToken.setOtpVerified(true);
    passwordResetTokenRepository.save(passwordResetToken);

    return Response.builder()
        .status(200)
        .message("OTP verified successfully")
        .otpVerified(true)
        .resetToken(resetToken)
        .build();
  }

  @Override
  @Transactional
  public Response resetPassword(ResetPasswordRequest resetPasswordRequest) {
    if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
      throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
    }

    PasswordResetToken passwordResetToken = passwordResetTokenRepository
        .findByTokenHashAndUsedFalse(hashValue(resetPasswordRequest.getResetToken()))
        .orElseThrow(() -> new IllegalArgumentException("Reset token is invalid"));

    if (!passwordResetToken.isOtpVerified()) {
      throw new IllegalArgumentException("Bạn cần xác thực mã OTP trước khi đổi mật khẩu");
    }

    if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Phiên đổi mật khẩu đã hết hạn");
    }

    User user = passwordResetToken.getUser();
    user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
    userRepository.save(user);

    expireActiveResetTokens(user.getId());

    return Response.builder()
        .status(200)
        .message("Password reset successfully")
        .build();
  }

  @Override
  public Response getAllUsers() {
    List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    users.forEach(user -> user.setTransaction(null));

    List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {
    }.getType());

    return Response.builder()
        .status(200)
        .message("success")
        .users(userDTOS)
        .build();
  }

  @Override
  public User getCurrentLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found"));

    user.setTransaction(null);

    return user;
  }

  @Override
  public Response getUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    userDTO.setTransaction(null);

    return Response.builder()
        .status(200)
        .message("success")
        .user(userDTO)
        .build();
  }

  @Override
  public Response updateUser(Long id, UserDTO userDTO) {
    User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

    if (userDTO.getEmail() != null)
      existingUser.setEmail(userDTO.getEmail());
    if (userDTO.getPhoneNumber() != null)
      existingUser.setPhoneNumber(userDTO.getPhoneNumber());
    if (userDTO.getName() != null)
      existingUser.setName(userDTO.getName());
    if (userDTO.getRole() != null)
      existingUser.setRole(userDTO.getRole());

    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
      existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

    if (userDTO.getIsActive() != null) {
      existingUser.setActive(userDTO.getIsActive());
    }

    userRepository.save(existingUser);

    auditLogService.logAction("UPDATE", "User", existingUser.getId(), "Cập nhật thông tin tài khoản: " + existingUser.getEmail());

    return Response.builder()
        .status(200)
        .message("User Successfully Updated")
        .build();
  }

  @Override
  public Response deleteUser(Long id) {
    User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

    // Thay vì xóa vĩnh viễn (Hard Delete), ta dùng Soft Delete (Khóa)
    existingUser.setActive(false);
    userRepository.save(existingUser);

    auditLogService.logAction("DELETE", "User", id, "Vô hiệu hóa (Khóa) tài khoản: " + existingUser.getEmail());

    return Response.builder()
        .status(200)
        .message("Tài khoản đã bị khóa thành công (Soft Delete)")
        .build();
  }

  @Override
  public Response getUserTransactions(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

    UserDTO userDTO = modelMapper.map(user, UserDTO.class);

    userDTO.getTransaction().forEach(transactionDTO -> {
      transactionDTO.setUser(null);
      transactionDTO.setSupplier(null);
    });

    return Response.builder()
        .status(200)
        .message("success")
        .user(userDTO)
        .build();
  }

  // vô hiệu hóa tất cả các token reset còn hiệu lực
  private void expireActiveResetTokens(Long userId) {
    List<PasswordResetToken> activeTokens = passwordResetTokenRepository.findByUserIdAndUsedFalse(userId);
    activeTokens.forEach(token -> token.setUsed(true));
    passwordResetTokenRepository.saveAll(activeTokens);
  }

  private String generateOtp() {
    int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
    return String.valueOf(otp);
  }

  private String generateResetToken() {
    return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
  }

  private String hashValue(String rawValue) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hashedToken = messageDigest.digest(rawValue.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashedToken);
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("Unable to hash sensitive value", ex);
    }
  }

}