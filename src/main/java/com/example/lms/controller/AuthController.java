package com.example.lms.controller;

import com.example.lms.dto.LoginDTO;
import com.example.lms.dto.RegisterDTO;
import com.example.lms.dto.AuthenticationDTO;
import com.example.lms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            return ResponseEntity.ok(userService.registerUser(registerDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            System.out.println("Login attempt with username: " + loginDTO.getUsername());
            AuthenticationDTO authenticationResponse = userService.loginUser(loginDTO);
            System.out.println("Login successful for username: " + loginDTO.getUsername());
            return ResponseEntity.ok(authenticationResponse);
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}