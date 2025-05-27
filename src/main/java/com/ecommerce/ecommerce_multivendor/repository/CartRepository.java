package com.ecommerce.ecommerce_multivendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecommerce_multivendor.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
}
