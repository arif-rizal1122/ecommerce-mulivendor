package com.ecommerce.ecommerce_multivendor.dto.request;

import lombok.Data;

@Data
public class LoginUserRequest {

    private String email;

    private String otp;

}
