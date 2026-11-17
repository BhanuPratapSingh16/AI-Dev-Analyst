package com.devanalyzer.backend.auth.service;

import com.devanalyzer.backend.auth.dto.*;
import com.devanalyzer.backend.exception.DuplicateResourceException;
import com.devanalyzer.backend.security.CustomUserDetails;
import com.devanalyzer.backend.security.CustomUserDetailsService;
import com.devanalyzer.backend.security.JwtService;
import com.devanalyzer.backend.user.entity.User;
import com.devanalyzer.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, CustomUserDetailsService customUserDetailsService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public RegisterResponse registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("User with email already exists!");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .id(savedUser.getId())
                .build();
    }

    public AuthResponse loginUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse
                .builder()
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .name(userDetails.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RefreshResponse refreshResponse(String refreshToken){
        String email = jwtService.extractEmail(refreshToken);

        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if(!jwtService.isRefreshTokenValid(refreshToken, email)){
            throw new RuntimeException("Refresh Token Invalid!");
        }

        return new RefreshResponse(jwtService.generateAccessToken(userDetails));
    }
}