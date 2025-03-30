package com.example.identity_service.services.impl;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.RefreshTokenRequest;
import com.example.identity_service.dto.respones.AuthenticationResponse;
import com.example.identity_service.entities.Role;
import com.example.identity_service.entities.User;
import com.example.identity_service.exceptions.TokenGenerationException;
import com.example.identity_service.repositories.UserRepository;
import com.example.identity_service.services.AuthenticationService;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    private static final String SECRET_KEY = "this-is-a-very-strong-and-long-secret-key"; // Use a secure key!

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var uses = userRepository.findAll();
         var use1 = userRepository.findByUsername("admin1");
;        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    /**
     * ✅ Generate JWT Token with user details.
     */
    public String generateToken(User user) {
        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            // Convert roles to a space-separated string
            String scope = user.getRoles().stream()
                    .map(Role::getName) // Extract role names
                    .collect(Collectors.joining(" "));

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("viet.phanq1")
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600000)) // 1-hour expiration
                    .claim("scope", scope) // Add user roles
                    .build();

            JWSSigner signer = new MACSigner(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//            JWSSigner signer = new MACSigner(Base64.getDecoder().decode(SECRET_KEY)); // Properly handle secret key
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();

        } catch (JOSEException e) {
            log.error("Error generating JWT: {}", e.getMessage()); // Log the error
            throw new TokenGenerationException("Error generating JWT", e); // Custom exception handling
        }
    }


    /**
     * ✅ Validate and parse JWT Token.
     */
    @Override
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if (!signedJWT.verify(verifier)) {
                return false;
            }

            return !isTokenExpired(signedJWT);

        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    /**
     * ✅ Extract Username from Token (Used in `JwtAuthenticationFilter`).
     */
    public String extractUsername(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Error extracting username from token", e);
        }
    }

    /**
     * ✅ Extract Roles from Token.
     */
    public List<String> extractRoles(String token) {
        try {
            String roles = SignedJWT.parse(token).getJWTClaimsSet().getStringClaim("scope");
            return List.of(roles.split(" "));
        } catch (ParseException e) {
            throw new RuntimeException("Error extracting roles from token", e);
        }
    }

    /**
     * ✅ Check if Token is Expired.
     */
    private boolean isTokenExpired(SignedJWT signedJWT) {
        try {
            return signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
        } catch (ParseException e) {
            throw new RuntimeException("Error checking token expiration", e);
        }
    }

    /**
     * ✅ Refresh JWT Token (User can get a new token using a valid old token).
     */
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String oldToken = request.getToken();
        if (!validateToken(oldToken)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String username = extractUsername(oldToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newToken = generateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(newToken)
                .build();
    }
    public boolean isTokenValid(String token, User userDetails) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            // ✅ Step 1: Verify token signature
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // ✅ Step 2: Check if token is expired
            if (isTokenExpired(signedJWT)) {
                return false;
            }

            // ✅ Step 3: Ensure token username matches userDetails
            String usernameFromToken = signedJWT.getJWTClaimsSet().getSubject();
            return usernameFromToken.equals(userDetails.getUsername());

        } catch (ParseException | JOSEException e) {
            return false;
        }
    }
}
