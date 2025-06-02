package com.ecommerce.ecommerce_multivendor.service.seller;

import java.util.List;

import com.ecommerce.ecommerce_multivendor.domain.AccountStatus;
import com.ecommerce.ecommerce_multivendor.exception.SellerException;
import com.ecommerce.ecommerce_multivendor.model.Seller;

public interface SellerService {
    
    Seller getSellerProfile(String jwt) throws Exception;

    Seller createSeller(Seller seller) throws Exception;

    Seller getSellerById(Long id) throws SellerException;

    Seller getSellerByEmail(String email) throws Exception;

    List<Seller>  getAllSellers(AccountStatus status);

    Seller updateSeller(Long id, Seller seller) throws Exception;

    Seller verifyEmail(String email, String otp) throws Exception;

    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;

    void deleteSeller(Long id) throws Exception;

}
