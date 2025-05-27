package com.ecommerce.ecommerce_multivendor.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {
    
    private String email;

    private String fullName;

    private String otp;

}
