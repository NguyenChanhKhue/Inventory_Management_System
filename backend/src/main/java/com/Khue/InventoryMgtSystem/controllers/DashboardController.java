package com.Khue.InventoryMgtSystem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import com.Khue.InventoryMgtSystem.repository.ProductRepository;
import com.Khue.InventoryMgtSystem.repository.TransactionRepository;
import com.Khue.InventoryMgtSystem.models.Product;
import com.Khue.InventoryMgtSystem.models.Transaction;
import com.Khue.InventoryMgtSystem.enums.TransactionType;
import com.Khue.InventoryMgtSystem.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        List<Product> products = productRepository.findAllActive();
        List<Transaction> transactions = transactionRepository.findAll();
        
        long totalImportVouchers = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.IMPORT && t.getTransactionStatus() == TransactionStatus.COMPLETED)
                .count();
                
        long totalExportVouchers = transactions.stream()
                .filter(t -> (t.getTransactionType() == TransactionType.EXPORT || t.getTransactionType() == TransactionType.RETURN_TO_SUPPLIER) 
                             && t.getTransactionStatus() == TransactionStatus.COMPLETED)
                .count();
        
        long totalStockItems = products.stream()
                .mapToLong(Product::getStockQuantity)
                .sum();
                
        BigDecimal totalInventoryValue = products.stream()
                .map(p -> p.getImportPrice() != null ? p.getImportPrice().multiply(BigDecimal.valueOf(p.getStockQuantity())) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        Map<String, Object> data = new HashMap<>();
        data.put("totalImportVouchers", totalImportVouchers);
        data.put("totalExportVouchers", totalExportVouchers);
        data.put("totalStockItems", totalStockItems);
        data.put("totalInventoryValue", totalInventoryValue);
        return ResponseEntity.ok(data);
    }
}
