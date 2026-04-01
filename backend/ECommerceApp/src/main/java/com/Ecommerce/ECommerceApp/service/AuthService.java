package com.Ecommerce.ECommerceApp.service;

import com.Ecommerce.ECommerceApp.Enums.Role;
import com.Ecommerce.ECommerceApp.dto.AuthResponse;
import com.Ecommerce.ECommerceApp.entity.User;
import com.Ecommerce.ECommerceApp.repository.UserRepository;
import com.Ecommerce.ECommerceApp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole(Role.ROLE_USER);

        userRepo.save(user);
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponse login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

//        String token = jwtUtil.generateToken(email);
        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );


    }


    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}