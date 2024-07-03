package com.bank.controller;

import com.bank.domain.User;
import com.bank.representation.LoginRepresentation;
import com.bank.service.AuthResponse;
import com.bank.service.JwtService;
import com.bank.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRepresentation request) throws AuthenticationException {
        authenticate(request.getUsername(), request.getPassword());

        final UserDetails userDetails = userInfoService.loadUserByUsername(request.getUsername());
        final String token = jwtService.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}

