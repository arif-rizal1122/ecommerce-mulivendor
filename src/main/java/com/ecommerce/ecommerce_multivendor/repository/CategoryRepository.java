package com.ecommerce.ecommerce_multivendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecommerce_multivendor.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryId(String categoryId);
    
}
