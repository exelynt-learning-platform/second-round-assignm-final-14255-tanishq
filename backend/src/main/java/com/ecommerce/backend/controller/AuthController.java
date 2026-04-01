package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.*;
import jakarta.validation.Valid;
import com.ecommerce.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }
}