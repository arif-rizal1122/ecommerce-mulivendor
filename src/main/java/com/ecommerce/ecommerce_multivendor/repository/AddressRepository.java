package com.ecommerce.ecommerce_multivendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecommerce_multivendor.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
