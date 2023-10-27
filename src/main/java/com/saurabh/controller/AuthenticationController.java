package com.saurabh.controller;

import com.saurabh.dto.*;
import com.saurabh.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }



    @PostMapping("/register")
    public ApiResponse<RegistrationResponseDTO> register(@RequestBody RegistrationRequestDTO request) {
        RegistrationResponseDTO response = authenticationService.register(request);
        return new ApiResponse<RegistrationResponseDTO>(
                true,
                "User Registered Successfully",
                response
        );
    }


    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequestDTO request) {
        AuthenticationResponse response =authenticationService.authenticate(request);
        return new ApiResponse<AuthenticationResponse>(
                true,
                "Authentication Successfully",
                response
        );
    }


    @PostMapping("/refreshtoken")
    public ApiResponse<NewAccessTokenResDTO> refreshAccessToken( @RequestBody NewAccessTokenReqDTO request) {
            NewAccessTokenResDTO response = authenticationService.refreshAccessToken(request.getRefreshToken());
        return new ApiResponse<NewAccessTokenResDTO>(
                true,
                "Access token generated",
                response
        );
    }
}
