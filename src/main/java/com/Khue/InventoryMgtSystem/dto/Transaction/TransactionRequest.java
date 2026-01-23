package com.Khue.InventoryMgtSystem.dto.Transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {
  @Positive(message = "product_id is required")
  private Long productId;

  @Positive(message = "quatity is required")
  private int quantity;

  @Positive(message = "supplier_id is required")
  private Long supplierId;

  private String description;

  private String note;
}