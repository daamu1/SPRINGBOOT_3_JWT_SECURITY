package com.saurabh.service;

import com.saurabh.dto.*;
import com.saurabh.enums.TokenType;
import com.saurabh.enums.UserRole;
import com.saurabh.exception.TokenException;
import com.saurabh.exception.UserAlreadyExistsException;
import com.saurabh.model.Role;
import com.saurabh.model.Token;
import com.saurabh.model.User;
import com.saurabh.repository.RoleRepository;
import com.saurabh.repository.TokenRepository;
import com.saurabh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegistrationResponseDTO register(RegistrationRequestDTO request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new UserAlreadyExistsException("User with the provided username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with the provided email already exists");
        }
        Set<UserRole> userRoles = request.getRole();
        Set<Role> roles = new HashSet<>();
        if (userRoles == null || userRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(UserRole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            for (UserRole userRole : userRoles) {
                Role role = roleRepository.findByName(userRole)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(role);
            }
        }
        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        User savedUser = userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        Token refreshToken = jwtService.createRefreshToken(userDetails.getId());
        String jwtToken = jwtService.generateToken(savedUser);

        return RegistrationResponseDTO
                .builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .roles(request.getRole())
                .type(TokenType.Bearer.toString())
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();

    }


    public AuthenticationResponse authenticate(AuthenticationRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User userDetails = (User) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);

            // Delete existing refresh tokens for the user
            jwtService.deleteByUserId(userDetails.getId());

            // Create a new refresh token
            Token refreshToken = jwtService.createRefreshToken(userDetails.getId());

            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshToken.getToken())
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Authentication failed: " + e.getMessage(), e);
        }
    }


    public NewAccessTokenResDTO refreshAccessToken(String refreshToken) {
        return tokenRepository.findByToken(refreshToken)
                .map(jwtService::verifyRefreshExpiration)
                .map(Token::getUser)
                .map(user -> {
                    String newAccessToken = jwtService.generateToken(user);
                    return new NewAccessTokenResDTO(newAccessToken, refreshToken);
                })
                .orElseThrow(() -> new TokenException("Refresh token is not valid or not found in the database."));
    }
}
