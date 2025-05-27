package com.ecommerce.ecommerce_multivendor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_multivendor.domain.USER_ROLE;
import com.ecommerce.ecommerce_multivendor.dto.request.SignUpRequest;
import com.ecommerce.ecommerce_multivendor.dto.response.AuthResponse;
import com.ecommerce.ecommerce_multivendor.service.auth.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth") 
public class AuthController {

    @Autowired 
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody SignUpRequest request) {
        AuthResponse response = new AuthResponse();
        try {
            String jwt = authService.createUser(request);
            response.setJwt(jwt);
            response.setMessage("User registered successfully!");
            response.setRole(USER_ROLE.ROLE_CUSTOMER);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // Status 201 Created
        } 
         catch (Exception e) {
            // Tangani error umum lainnya
            response.setMessage("Registration failed due to an unexpected error: " + e.getMessage());
            response.setJwt(null);
            response.setRole(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Status 500 Internal Server Error
        }
    }
}