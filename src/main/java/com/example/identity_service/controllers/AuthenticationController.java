package com.example.identity_service.controllers;


import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.InstroRespectRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.respones.ApiResponse;
import com.example.identity_service.dto.respones.AuthenticationResponse;
import com.example.identity_service.dto.respones.InstroRespectResponse;
import com.example.identity_service.dto.respones.UserResponse;
import com.example.identity_service.entities.User;
import com.example.identity_service.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody @Valid AuthenticationRequest request) {
        var authenticationResponse = authenticationService.authenticate(request);
        if(authenticationResponse.isAuthenticated()) {
            return ResponseEntity.ok(ApiResponse.success(authenticationResponse));
        }
        return ResponseEntity.ok(ApiResponse.error("unauthenticated"));
    }
    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<InstroRespectResponse>> validateToken(@RequestBody InstroRespectRequest request) {
        boolean isValid = authenticationService.validateToken(request.getToken());

        if (isValid) {
            return ResponseEntity.ok(
                    ApiResponse.success(InstroRespectResponse.builder().valid(true).build())
            );
        }

        return ResponseEntity.ok(ApiResponse.error("invalid_token"));
    }

}
