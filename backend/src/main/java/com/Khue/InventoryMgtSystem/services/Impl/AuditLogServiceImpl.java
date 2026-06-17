package com.Khue.InventoryMgtSystem.services.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Khue.InventoryMgtSystem.dto.AuditLogDTO;
import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.models.AuditLog;
import com.Khue.InventoryMgtSystem.repository.AuditLogRepository;
import com.Khue.InventoryMgtSystem.services.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ModelMapper modelMapper;

    @Override
    public void logAction(String action, String entityName, Long entityId, String details) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = "SYSTEM";
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                userName = authentication.getName(); // Usually email
            }

            AuditLog logEntry = AuditLog.builder()
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .userName(userName)
                .details(details)
                .createdAt(LocalDateTime.now())
                .build();

            auditLogRepository.save(logEntry);
        } catch (Exception e) {
            log.error("Failed to save audit log: ", e);
        }
    }

    @Override
    public Response getAllAuditLogs() {
        List<AuditLog> logs = auditLogRepository.findAllByOrderByCreatedAtDesc();
        List<AuditLogDTO> logDTOs = modelMapper.map(logs, new TypeToken<List<AuditLogDTO>>() {}.getType());

        return Response.builder()
            .status(200)
            .message("success")
            .auditLogs(logDTOs)
            .build();
    }
}
