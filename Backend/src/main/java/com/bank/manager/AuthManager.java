package com.bank.manager;

import com.bank.representation.LoginRepresentation;
import com.bank.service.JwtService;
import com.bank.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthManager {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;

    public String login(LoginRepresentation loginRepresentation){
        authenticate(loginRepresentation.getUsername(), loginRepresentation.getPassword());

        final UserDetails userDetails = userInfoService.loadUserByUsername(loginRepresentation.getUsername());
        return jwtService.generateToken(userDetails.getUsername());
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
