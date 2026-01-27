package com.Khue.InventoryMgtSystem.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.persistence.Id;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@AllArgsConstructor

@Table(name="products")
@Data
@Builder
public class Product {
  @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "SKU is required") // stock keeping unit
  @Column(unique = true)
  private String sku;

  @Positive(message = "product price must be a posotive value")
  private BigDecimal price;

  @Min(value = 0 , message = "stock quantity cannot be negative value")
  private int stockQuantity;

  private String descripton;

  private LocalDateTime expDateTime;
  
  private String imgUrl;

  private final LocalDateTime createAt = LocalDateTime.now();

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category; // product N - 1 category

  @Override
  public String toString() {
    return "Product [id=" + id + ", name=" + name + ", sku=" + sku + ", price=" + price + ", stockQuantity="
        + stockQuantity + ", descripton=" + descripton + ", expDateTime=" + expDateTime + ", imgUrl=" + imgUrl
        + ", createAt=" + createAt + "]";
  }


}
