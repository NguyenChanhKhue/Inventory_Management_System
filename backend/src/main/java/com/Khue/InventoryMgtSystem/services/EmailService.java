package com.Khue.InventoryMgtSystem.services;

public interface EmailService {
    void sendPasswordResetOtp(String toEmail, String fullName, String otp);
}
