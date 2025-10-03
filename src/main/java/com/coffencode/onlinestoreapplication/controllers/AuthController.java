package com.coffencode.onlinestoreapplication.controllers;

import com.coffencode.onlinestoreapplication.dto.AuthResponse;
import com.coffencode.onlinestoreapplication.dto.CustomerAuthDTO;
import com.coffencode.onlinestoreapplication.dto.LoginRequest;
import com.coffencode.onlinestoreapplication.dto.SignupRequest;
import com.coffencode.onlinestoreapplication.repositories.CustomerRepository;
import com.coffencode.onlinestoreapplication.security.JwtUtil;
import com.coffencode.onlinestoreapplication.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(CustomerRepository customerRepository,
                          CustomerService customerService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Signup: delegate to CustomerService so the service creates customer + cart.
     * Returns JWT + minimal customer info (CustomerAuthDTO).
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        // duplicate email check
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        // create customer (service handles cart creation)
        CustomerAuthDTO created = customerService.createCustomerForAuth(request);

        // generate token — JwtUtil uses username (subject). We build a lightweight UserDetails.
        UserDetails userDetails = User.withUsername(created.getEmail())
                .password("") // password not used to create token
                .authorities(java.util.Collections.emptyList())
                .build();

        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token, created));
    }

    /**
     * Login: authenticate via AuthenticationManager, then build token and return lean DTO.
     * Note: repository method findAuthDTOByEmail avoids loading cart/orders.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        CustomerAuthDTO dto = customerRepository.findAuthDTOByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Customer not found after authentication"));

        return ResponseEntity.ok(new AuthResponse(token, dto));
    }
}
