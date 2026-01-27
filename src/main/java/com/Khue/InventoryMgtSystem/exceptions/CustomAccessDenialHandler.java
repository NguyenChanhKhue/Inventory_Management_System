package com.Khue.InventoryMgtSystem.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.Khue.InventoryMgtSystem.dto.Response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class CustomAccessDenialHandler implements AccessDeniedHandler{
  // Xử lý trường hợp người dùng đã đăng nhập nhưng không có quyền để sử dụng tài nguyên đang truy cập
  private final ObjectMapper objectMapper;
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    Response errorResponse = Response.builder()
                              .status(HttpStatus.FORBIDDEN.value())
                              .message(accessDeniedException.getMessage())
                              .build();

    response.setContentType("application/json");
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));   // chuyển response về json và ghi chuỗi đó vào HTTP            
  
  }
  
}
