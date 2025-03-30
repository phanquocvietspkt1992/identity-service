package com.example.identity_service.services;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.InstroRespectRequest;
import com.example.identity_service.dto.request.RefreshTokenRequest;
import com.example.identity_service.dto.respones.AuthenticationResponse;
import com.example.identity_service.entities.User;

public interface AuthenticationService {

    /**
     * Authenticate user credentials and return a JWT token.
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Validate JWT token.
     */
    boolean validateToken(String token);

    /**
     * Refresh an expired or soon-to-expire token.
     */
    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    /**
     * Extract username from a given JWT token.
     */
    String extractUsername(String token);

    boolean isTokenValid(String token, User userDetails);
}
