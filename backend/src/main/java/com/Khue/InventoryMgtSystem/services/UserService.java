package com.Khue.InventoryMgtSystem.services;

import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.dto.UserDTO;
import com.Khue.InventoryMgtSystem.dto.Auth.ForgotPasswordRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.LoginRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.RegisterRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.ResetPasswordRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.VerifyOtpRequest;
import com.Khue.InventoryMgtSystem.models.User;

public interface UserService {
    Response registerUser (RegisterRequest registerRequest);

    Response loginUser (LoginRequest loginRequest);

    Response forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Response verifyPasswordResetOtp(VerifyOtpRequest verifyOtpRequest);

    Response resetPassword(ResetPasswordRequest resetPasswordRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id , UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}
