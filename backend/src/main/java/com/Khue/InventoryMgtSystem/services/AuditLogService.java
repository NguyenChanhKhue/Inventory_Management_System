package com.Khue.InventoryMgtSystem.services;

import com.Khue.InventoryMgtSystem.dto.Response;

public interface AuditLogService {
    void logAction(String action, String entityName, Long entityId, String details);
    Response getAllAuditLogs();
}
