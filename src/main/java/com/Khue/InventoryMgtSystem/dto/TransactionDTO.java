package com.Khue.InventoryMgtSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;



import org.springframework.transaction.TransactionStatus;

import com.Khue.InventoryMgtSystem.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;



import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

	private Long id;

  private int totalProduct;

  private BigDecimal totalPrice;

  private TransactionType transactionType;

  private TransactionStatus transactionStatus;

  private String description;

  private String note ;

  private  LocalDateTime createAt;

  private  LocalDateTime updateAt;



  private UserDTO user;


  private ProductDTO product;


  private SupplierDTO supplier;



}
