package com.ecommerce.ecommerce_multivendor.service.auth;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_multivendor.dto.request.SignUpRequest;

@Service
public interface AuthService {
    
    String createUser(SignUpRequest request);

}
