package com.Khue.InventoryMgtSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Khue.InventoryMgtSystem.models.Product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product , Long>{
  
  @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.id DESC")
  List<Product> findAllActive();

  @Query("SELECT p FROM Product p WHERE p.isActive = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
  List<Product> searchActiveProducts(@Param("searchTerm") String searchTerm);

  @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stockQuantity <= p.minStock ORDER BY p.stockQuantity ASC")
  List<Product> findLowStockProducts();
}
