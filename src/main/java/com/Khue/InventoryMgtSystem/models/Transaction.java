package com.Khue.InventoryMgtSystem.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.transaction.TransactionStatus;

import com.Khue.InventoryMgtSystem.enums.TransactionType;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@AllArgsConstructor

@Table(name="transactions")
@Data
@Builder
public class Transaction {
  @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

  private int totalProduct;

  private BigDecimal totalPrice;
  

  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;

  @Enumerated(EnumType.STRING)
  private TransactionStatus transactionStatus;

  private String description;

  private String note ;

  @Column(name = "create_at")
  private final LocalDateTime createAt =  LocalDateTime.now();

  @Column(name = "update_at")
  private final LocalDateTime updateAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id")
  private Supplier supplier;

  @Override
  public String toString() {
    return "Transaction [id=" + id + ", totalProduct=" + totalProduct + ", totalPrice=" + totalPrice
        + ", transactionType=" + transactionType + ", transactionStatus=" + transactionStatus + ", description="
        + description + ", note=" + note + ", createAt=" + createAt + ", updateAt=" + updateAt + "]";
  }

}
