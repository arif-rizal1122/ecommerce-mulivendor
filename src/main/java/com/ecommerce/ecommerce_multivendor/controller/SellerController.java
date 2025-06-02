package com.ecommerce.ecommerce_multivendor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_multivendor.config.JwtProvider;
import com.ecommerce.ecommerce_multivendor.domain.AccountStatus;
import com.ecommerce.ecommerce_multivendor.dto.request.LoginUserRequest;
import com.ecommerce.ecommerce_multivendor.dto.response.AuthResponse;
import com.ecommerce.ecommerce_multivendor.exception.SellerException;
import com.ecommerce.ecommerce_multivendor.model.Seller;
import com.ecommerce.ecommerce_multivendor.model.VerificationCode;
import com.ecommerce.ecommerce_multivendor.repository.VerificationCodeRepository;
import com.ecommerce.ecommerce_multivendor.service.auth.AuthService;
import com.ecommerce.ecommerce_multivendor.service.auth.EmailService;
import com.ecommerce.ecommerce_multivendor.service.seller.SellerService;
import com.ecommerce.ecommerce_multivendor.utils.OtpUtil;

// import jakarta.validation.Valid;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtProvider jwtProvider;


  @PostMapping("/login")
  public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginUserRequest request) throws Exception {
        String otp = request.getOtp();
        String email = request.getEmail();

        request.setEmail("seller_"+email);
        System.out.println(otp+"- "+email);
        AuthResponse authResponse = authService.signing(request);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
        @PathVariable String otp
    ) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("wrong otp ...");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


      @PostMapping("/create")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller)
      throws Exception
    {
        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());

        verificationCodeRepository.save(verificationCode);
        String subject = "Login/Signup OTP Verification";
        String text = "Your login/signup OTP is: " + otp;
        String frontend_url = "http://localhost:8081/verify-seller";

        emailService.sendVerificationOtpEmail(seller.getEmail(), otp, subject, text, frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException{
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception{
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


    // @GetMapping("/report")
    // public ResponseEntity<SellerReport> getSellerReport(
    //     @RequestHeader("Authorization") String jwt
    // ){

    //   Seller seller = sellerService

    // }


    @GetMapping("/all")
    public ResponseEntity<List<Seller>> getAllSellers(
        @RequestParam(required = false) AccountStatus status
    ) throws Exception {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers); 
    }



    @PatchMapping("/update")
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception{
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatSeller);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception{
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
