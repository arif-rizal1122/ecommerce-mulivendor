package com.ecommerce.ecommerce_multivendor.service.user;

import com.ecommerce.ecommerce_multivendor.model.User;

public interface UserService {
    
    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;

}
