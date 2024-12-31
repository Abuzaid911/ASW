package com.example.lms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;

    public void setCreatedAt(LocalDateTime createdAt) {
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
    }

    public void setActive(boolean active) {
    }
}