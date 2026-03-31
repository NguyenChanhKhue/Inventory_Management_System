package com.Khue.InventoryMgtSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Khue.InventoryMgtSystem.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
  
}
