package com.ecommerce.ecommerce_multivendor.service.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.ecommerce_multivendor.dto.request.CreateProductRequest;
import com.ecommerce.ecommerce_multivendor.exception.ProductException;
import com.ecommerce.ecommerce_multivendor.model.Product;
import com.ecommerce.ecommerce_multivendor.model.Seller;

public interface ProductService {

    Product createProduct(CreateProductRequest request, Seller seller);

    void deleteProduct(Long productId) throws ProductException;

    Product updateProduct(Long productId, Product product) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    List<Product> searchProducts(String query);

    Page<Product> getAllProduct(String category, 
                                String brand, 
                                String colors, 
                                String sizes,
                                Integer minPrize,
                                Integer maxPrize,
                                Integer minDiscount,
                                String sort,
                                String stock,
                                Integer pageNumber
    );

    List<Product> getProductBySellerId(Long sellerId);

    

    
}
