package com.Khue.InventoryMgtSystem.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

  // Chung
  private int status;
  private String message;

  //Login
  private String token;
  private String role;
  private String expirationTime;

  //Pagination
  private int totalPages;
  private Long totalElements;

  // data output optional (co the 1 hoac ca 2)
  private UserDTO user; // 1 user
  private List<UserDTO> users; // 1 list user

  private ProductDTO product;
  private List<ProductDTO> products; 

  private SupplierDTO supplier;
  private List<SupplierDTO> suppliers; 

  private CategoryDTO category;
  private List<CategoryDTO> categories; 

  private TransactionDTO transaction;
  private List<TransactionDTO> transactions; 

  private final LocalDateTime timeStamp = LocalDateTime.now();

}
