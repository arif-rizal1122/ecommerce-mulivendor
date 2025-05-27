package com.ecommerce.ecommerce_multivendor.dto.response;

import com.ecommerce.ecommerce_multivendor.domain.USER_ROLE;

import lombok.Data;

@Data
public class AuthResponse {
    
    private String jwt;

    private String message;

    private USER_ROLE role;

}
