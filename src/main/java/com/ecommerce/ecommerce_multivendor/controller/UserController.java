package com.ecommerce.ecommerce_multivendor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_multivendor.model.User;
import com.ecommerce.ecommerce_multivendor.service.user.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception{
            User user = userService.findUserByJwtToken(jwt);
            return ResponseEntity.ok(user);
    }



    // @GetMapping("/profile")
    // public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception{
    //         User user = userService.findUserByJwtToken(jwt);
    //         return ResponseEntity.ok(user);
    // }
    
}
