package com.Khue.InventoryMgtSystem.dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;
}
