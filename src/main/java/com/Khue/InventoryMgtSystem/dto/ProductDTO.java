package com.Khue.InventoryMgtSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // tra ve cac field khong NULL
@JsonIgnoreProperties(ignoreUnknown = true) // Bo qua cac field khong ton tai
public class ProductDTO {

	private Long id;

  private Long categoryId;
  private Long productId;
  private Long supplierId;

  private String name;

  private String sku;

  private BigDecimal price;
  
  private int stockQuantity;

  private String descripton;

  private LocalDateTime expDateTime;

  private String imgUrl;
  private LocalDateTime createAt;


}
