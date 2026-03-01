package com.Khue.InventoryMgtSystem.services.Impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.dto.UserDTO;
import com.Khue.InventoryMgtSystem.dto.Auth.LoginRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.RegisterRequest;
import com.Khue.InventoryMgtSystem.enums.UserRole;
import com.Khue.InventoryMgtSystem.exceptions.InvalidCredentialsException;
import com.Khue.InventoryMgtSystem.exceptions.NotFoundException;
import com.Khue.InventoryMgtSystem.models.User;
import com.Khue.InventoryMgtSystem.repository.UserRepository;
import com.Khue.InventoryMgtSystem.security.JwtUtils;
import com.Khue.InventoryMgtSystem.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;
  private final JwtUtils jwtUtils;

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

    return Response.builder()
        .status(200)
        .message("User was successfully registed")
        .build();
  }

  @Override
  public Response loginUser(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new NotFoundException("Email Not Found"));

    // check password
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Password Does Not Match ");
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
  public Response getAllUsers() {

    List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    users.forEach(user -> user.setTransaction(null));

    // map user -> userDTO
    List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {
    }.getType());

    return Response.builder()
        .status(200)
        .message("success")
        .users(userDTOS)
        .build();
  }

  // Lấy user hiện tại đang đăng nhập
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

    if(userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
    if(userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
    if(userDTO.getName() != null) existingUser.setName(userDTO.getName());
    if(userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

    if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
      existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }
    userRepository.save(existingUser);
    return Response.builder()
                  .status(200)
                  .message("User Successfully Updated")
                  .build();
  }

  @Override
  public Response deleteUser(Long id) {
    userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

    userRepository.deleteById(id);

    return Response.builder()
                .status(200)
                .message("User Successfully Deleted")
                .build();
  }

  // lay giao dich cua user
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
}
