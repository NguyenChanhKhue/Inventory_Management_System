package com.Khue.InventoryMgtSystem.dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "otp is required")
    @Pattern(regexp = "\\d{6}", message = "otp must be exactly 6 digits")
    private String otp;
}
