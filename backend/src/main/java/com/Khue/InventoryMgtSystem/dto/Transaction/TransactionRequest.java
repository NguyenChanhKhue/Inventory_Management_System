package com.Khue.InventoryMgtSystem.dto.Transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {
  @NotNull(message = "Vui lòng chọn Sản phẩm")
  private Long productId;

  @Min(value = 0, message = "Số lượng không được nhỏ hơn 0")
  private int quantity;

  private Long supplierId;

  private String description;

  private String note;

}