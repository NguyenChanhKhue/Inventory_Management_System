package com.Khue.InventoryMgtSystem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.dto.Auth.ForgotPasswordRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.LoginRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.RegisterRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.ResetPasswordRequest;
import com.Khue.InventoryMgtSystem.dto.Auth.VerifyOtpRequest;
import com.Khue.InventoryMgtSystem.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity <Response> registerUser(@RequestBody @Valid RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest){
        return ResponseEntity.ok(userService.forgotPassword(forgotPasswordRequest));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Response> verifyOtp(@RequestBody @Valid VerifyOtpRequest verifyOtpRequest){
        return ResponseEntity.ok(userService.verifyPasswordResetOtp(verifyOtpRequest));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest){
        return ResponseEntity.ok(userService.resetPassword(resetPasswordRequest));
    }
}
