package com.ecommerce.ecommerce_multivendor.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_multivendor.config.JwtProvider;
import com.ecommerce.ecommerce_multivendor.model.User;
import com.ecommerce.ecommerce_multivendor.repository.UserRepository;

@Service
public class UserServiceIMpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    
    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user==null) {
            throw new Exception("user not found with email - "+ email);
        }
        return user;
    }




    @Override
    public User findUserByJwtToken(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = this.findUserByEmail(email);
        if (user==null) {
            throw new Exception("user not found with email - " + email);
        }
        return user;
    }
    
    
}
