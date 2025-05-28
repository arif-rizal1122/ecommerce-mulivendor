package com.ecommerce.ecommerce_multivendor.service.auth;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_multivendor.dto.request.LoginUserRequest;
import com.ecommerce.ecommerce_multivendor.dto.request.OtpRequest;
import com.ecommerce.ecommerce_multivendor.dto.request.SignUpRequest;
import com.ecommerce.ecommerce_multivendor.dto.response.AuthResponse;

@Service
public interface AuthService {

    void sendLoginOtp(OtpRequest email) throws Exception;
    
    String createUser(SignUpRequest request) throws Exception;

    AuthResponse signin(LoginUserRequest request) throws Exception;

}
