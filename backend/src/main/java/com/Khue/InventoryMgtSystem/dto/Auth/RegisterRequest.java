package com.Khue.InventoryMgtSystem.dto.Auth;

import com.Khue.InventoryMgtSystem.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @NotBlank(message = "Vui lòng nhập họ và tên")
  private String name;

  @NotBlank(message = "Vui lòng nhập địa chỉ email")
  @Email(message = "Định dạng email không hợp lệ")
  private String email;

  @NotBlank(message = "Vui lòng nhập mật khẩu")
  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String password;

  @NotBlank(message = "Vui lòng nhập số điện thoại")
  private String phoneNumber;

  private UserRole role ;
} 
