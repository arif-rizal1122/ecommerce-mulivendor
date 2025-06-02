package com.ecommerce.ecommerce_multivendor.service.auth;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_multivendor.domain.USER_ROLE;
import com.ecommerce.ecommerce_multivendor.dto.request.LoginUserRequest;
import com.ecommerce.ecommerce_multivendor.dto.request.SignUpRequest;
import com.ecommerce.ecommerce_multivendor.dto.response.AuthResponse;

@Service
public interface AuthService {

    void sendLoginOtp(String email, USER_ROLE role) throws Exception;
    
    String createUser(SignUpRequest request) throws Exception;

    AuthResponse signing(LoginUserRequest request) throws Exception;

}
