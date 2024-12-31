package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private String type;
    private Boolean read;
    private LocalDateTime createdAt;
    private Long userId;
}