package com.Khue.InventoryMgtSystem.dto.Auth;

import com.Khue.InventoryMgtSystem.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "email is required")
  private String email;

  @NotBlank(message = "password is required")
  private String password;

  @NotBlank(message = "phone number is required")
  private String phoneNumber;

  private UserRole role ;
} 
