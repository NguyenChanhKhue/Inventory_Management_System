package com.Khue.InventoryMgtSystem.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.Khue.InventoryMgtSystem.dto.Response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;


@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
  // Khi có 1 request đến , mà chưa được authenticate , spring sẽ gọi class này để phản hồi về user
  // User truy cập vào tài nguyên được bảo vệ mà chưa đăng nhập , ... sẽ gọi class này


  private final ObjectMapper objectMapper; // chuyển java-Json trả về client
  @Override
  public void commence(HttpServletRequest request,
                      HttpServletResponse response, AuthenticationException authException)
                      throws IOException, ServletException {
    Response errorResponse = Response.builder()
                              .status(HttpStatus.UNAUTHORIZED.value())
                              .message(authException.getMessage())
                              .build();
    response.setContentType("application/json");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));   // chuyển response về json và ghi chuỗi đó vào HTTP            
  
  }
}
