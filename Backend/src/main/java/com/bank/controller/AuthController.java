package com.bank.controller;

import com.bank.manager.AuthManager;
import com.bank.representation.LoginRepresentation;
import com.bank.service.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRepresentation request) throws AuthenticationException {
        String token = authenticationManager.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

