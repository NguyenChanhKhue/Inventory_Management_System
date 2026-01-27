package com.Khue.InventoryMgtSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Khue.InventoryMgtSystem.models.Product;

public interface ProductRepository extends JpaRepository<Product , Long>{
  List <Product> findByNameOrDescription(String name , String description);
}
