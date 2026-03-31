package com.Khue.InventoryMgtSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.Khue.InventoryMgtSystem.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction , Long> , JpaSpecificationExecutor<Transaction>{
 //JpaSpecificationExecutor<Transaction> : cho phep loc bang nhieu field (tu dinh nghia), khong can phai viet nhieu method
}
