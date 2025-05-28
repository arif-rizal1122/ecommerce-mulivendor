package com.ecommerce.ecommerce_multivendor.service.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.ecommerce_multivendor.config.JwtProvider;
import com.ecommerce.ecommerce_multivendor.domain.USER_ROLE;
import com.ecommerce.ecommerce_multivendor.dto.request.LoginUserRequest;
import com.ecommerce.ecommerce_multivendor.dto.request.OtpRequest;
import com.ecommerce.ecommerce_multivendor.dto.request.SignUpRequest;
import com.ecommerce.ecommerce_multivendor.dto.response.AuthResponse;
import com.ecommerce.ecommerce_multivendor.model.Cart;
import com.ecommerce.ecommerce_multivendor.model.User;
import com.ecommerce.ecommerce_multivendor.model.VerificationCode;
import com.ecommerce.ecommerce_multivendor.repository.CartRepository;
import com.ecommerce.ecommerce_multivendor.repository.UserRepository;
import com.ecommerce.ecommerce_multivendor.repository.VerificationCodeRepository;
import com.ecommerce.ecommerce_multivendor.utils.OtpUtil;


@Service
public class AuthServiceIMpl implements AuthService{

    

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomUserServiceImpl customUserServiceImpl;



    @Override
    public void sendLoginOtp(OtpRequest request) throws Exception {
        String SIGNING_PREFIX = "signin_";
        String email = request.getEmail(); // Ambil email dari request
        
        // PERBAIKI: Logic untuk memproses email dengan prefix
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length()); // Assign ke variable email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("user not exists with provided email");
            }
        }
        
        // Gunakan email yang sudah diproses
        VerificationCode isExists = verificationCodeRepository.findByEmail(email);
        if (isExists != null) {
            verificationCodeRepository.delete(isExists);
        }
        
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email); // Gunakan email yang sudah diproses
        verificationCodeRepository.save(verificationCode);

        String subject = "arif rizal login/signup";
        String text = "your login / signup OTP is: " + otp; // Perbaiki text untuk include OTP

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }




    @Override
    @Transactional
    public String createUser(SignUpRequest request) throws Exception {

    VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
    
    if (verificationCode==null || !verificationCode.getOtp().equals(request.getOtp())) {
        throw new Exception("wrong otp...");
    } 

    User user = userRepository.findByEmail(request.getEmail());
    if (user==null) {
            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setFullName(request.getFullName());
            newUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            newUser.setMobile("12345678");
            newUser.setPassword(passwordEncoder.encode(request.getOtp()));

            user = userRepository.save(newUser);
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
    } 

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
    Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return jwtProvider.generateToken(authentication);
    }




    @Override
    public AuthResponse signin(LoginUserRequest request) throws Exception {
        String username = request.getEmail();
        String otp = request.getOtp();

        // Validasi input
        if (username == null || username.trim().isEmpty()) {
            throw new BadCredentialsException("Email cannot be empty");
        }
        if (otp == null || otp.trim().isEmpty()) {
            throw new BadCredentialsException("OTP cannot be empty");
        }

        Authentication authentication = authentication(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(token);
        authResponse.setMessage("login successfully");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        if (roleName != null) {
            authResponse.setRole(USER_ROLE.valueOf(roleName));
        }
        
        return authResponse;
    }

    private Authentication authentication(String username, String otp) throws Exception {
        try {
            UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
            
            if (userDetails == null) {
                throw new BadCredentialsException("Invalid username or password");
            }

            // Cek verifikasi OTP
            VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
            if (verificationCode == null) {
                throw new BadCredentialsException("No verification code found for this email");
            }
            
            if (!verificationCode.getOtp().equals(otp)) {
                throw new BadCredentialsException("Invalid OTP");
            }

            return new UsernamePasswordAuthenticationToken(userDetails, null, 
                                                        userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("User not found with email: " + username);
        }
    }
    
}   
