package com.Khue.InventoryMgtSystem.models;

import java.util.List;



import jakarta.persistence.CascadeType;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@AllArgsConstructor

@Table(name="categories")
@Data
@Builder
public class Category {
  @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

  @NotBlank(message = "name is required")
  private String name;


  @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL)
  private List<Product> products;


  @Override
  public String toString() {
    return "Category [id=" + id + ", name=" + name + "]";
  }


  

}
