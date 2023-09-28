package com.saurabh.myapp.controller;

import com.saurabh.dto.ApiResponse;
import com.saurabh.dto.AuthenticationRequestDTO;
import com.saurabh.dto.RegistrationRequestDTO;
import com.saurabh.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Register a new user.
     *
     * @param request The registration request containing user details.
     * @return ResponseEntity with an ApiResponse as the response body.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register( @RequestBody RegistrationRequestDTO request) {
        ApiResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate a user.
     *
     * @param request The authentication request containing user credentials.
     * @return ResponseEntity with an ApiResponse as the response body.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody AuthenticationRequestDTO request) {
        ApiResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh an authentication token.
     *
     * @param request  The HTTP request object.
     * @param response The HTTP response object.
     * @throws IOException If there's an I/O error during token refresh.
     */
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
