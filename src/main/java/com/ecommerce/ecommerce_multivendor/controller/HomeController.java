package com.ecommerce.ecommerce_multivendor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping
    public String HomeControllerHandler(){
        return "welcome to my channel";
    }


}
