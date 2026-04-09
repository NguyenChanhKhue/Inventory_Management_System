package com.Khue.InventoryMgtSystem.services.Impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Khue.InventoryMgtSystem.services.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendPasswordResetOtp(String toEmail, String fullName, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Inventory Management System - Password Reset OTP");
        message.setText(buildEmailBody(fullName, otp));
        javaMailSender.send(message);
    }

    private String buildEmailBody(String fullName, String otp) {
        return "Hello " + fullName + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Your OTP code is: " + otp + "\n\n"
                + "This code will expire in 5 minutes.\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Best regards,\nInventory Management System";
    }
}
