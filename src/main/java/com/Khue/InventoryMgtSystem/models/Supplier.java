package com.Khue.InventoryMgtSystem.models;


import org.springframework.data.annotation.Id;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@AllArgsConstructor

@Table(name="suppliers")
@Data
@Builder
public class Supplier {
  @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

  @NotBlank(message = "name is required")
  private String name;


  @NotBlank(message = "contactInfo is required")
  private String contactInfo;

  private String address;


}
