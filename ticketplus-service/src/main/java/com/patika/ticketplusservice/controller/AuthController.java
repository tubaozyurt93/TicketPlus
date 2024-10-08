package com.patika.ticketplusservice.controller;

import com.patika.ticketplusservice.request.LoginRequest;
import com.patika.ticketplusservice.request.UserRequest;
import com.patika.ticketplusservice.response.TokenResponse;
import com.patika.ticketplusservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AuthController {

    Logger logger = Logger.getLogger(AuthController.class.getName());


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest registerRequest) throws Exception {
        logger.log(Level.INFO, "[AuthController] -- user created: " + registerRequest);
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.log(Level.INFO, "[AuthController] -- user login: " + loginRequest);
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
