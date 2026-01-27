package com.Khue.InventoryMgtSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Khue.InventoryMgtSystem.models.User;

public interface UserRepository extends JpaRepository<User , Long>{
  Optional <User> findByEmail(String email);
} 
