package com.Khue.InventoryMgtSystem.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action; // CREATE, UPDATE, DELETE

    @Column(nullable = false)
    private String entityName; // Product, Category, Supplier, Transaction

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private String userName; // Email or Name of the user performing the action

    @Column(length = 1000)
    private String details;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
