package com.ecommerce.ecommerce_multivendor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_multivendor.dto.response.ApiResponse;

@RestController
public class HomeController {
    
    @GetMapping
    public ApiResponse HomeControllerHandler(){
        ApiResponse response = new ApiResponse();
        response.setMessage("welcome to ecommerce multivendor system");
        return response;
    }


}
