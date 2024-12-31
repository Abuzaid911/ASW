package com.example.lms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonOTPDTO {
    private String otpCode;
    private LocalDateTime expiryTime;
}