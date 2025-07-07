package com.ecommerce.ecommerce_multivendor.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_multivendor.dto.request.CreateProductRequest;
import com.ecommerce.ecommerce_multivendor.model.Product;
import com.ecommerce.ecommerce_multivendor.model.Seller;
import com.ecommerce.ecommerce_multivendor.service.product.ProductService;
import com.ecommerce.ecommerce_multivendor.service.seller.SellerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequestMapping("/sellers/products")
@RestController
public class SellerProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySelerId(
         @RequestHeader("Authorization") String jwt
    ) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<Product> createProduct(
        @RequestBody CreateProductRequest request,
        @RequestHeader("Authorization") String jwt
    ) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(request, seller);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){

        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product)
    {
        try {
            Product updatedProduct = productService.updateProduct(productId, product);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
