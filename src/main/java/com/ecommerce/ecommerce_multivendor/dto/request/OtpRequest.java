package com.ecommerce.ecommerce_multivendor.dto.request;

import lombok.Data;

import com.ecommerce.ecommerce_multivendor.domain.USER_ROLE;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class OtpRequest { // Nama kelas DTO yang jelas

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    private String otp;

    private USER_ROLE role;

}