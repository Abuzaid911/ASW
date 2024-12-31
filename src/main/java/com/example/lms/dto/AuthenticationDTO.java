package com.example.lms.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;

    public AuthenticationDTO(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}