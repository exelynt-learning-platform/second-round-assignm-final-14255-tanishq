package com.Ecommerce.ECommerceApp.controller;

import com.Ecommerce.ECommerceApp.entity.Cart;
import com.Ecommerce.ECommerceApp.entity.User;
import com.Ecommerce.ECommerceApp.service.AuthService;
import com.Ecommerce.ECommerceApp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final AuthService authService;

    @GetMapping("/retrieve")
    public Cart getCart(Authentication auth) {
        User user = authService.getUserByEmail(auth.getName());
        return cartService.getCart(user);
    }

    @PostMapping("/add")
    public Cart addProduct(Authentication auth,
                           @RequestParam Long productId,
                           @RequestParam int quantity) {
        User user = authService.getUserByEmail(auth.getName());
        return cartService.addProduct(user, productId, quantity);
    }

    @PutMapping("/update")
    public Cart updateProduct(Authentication auth,
                              @RequestParam Long productId,
                              @RequestParam int quantity) {
        User user = authService.getUserByEmail(auth.getName());
        return cartService.updateProduct(user, productId, quantity);
    }

    @DeleteMapping("/remove")
    public Cart removeProduct(Authentication auth,
                              @RequestParam Long productId) {
        User user = authService.getUserByEmail(auth.getName());
        return cartService.removeProduct(user, productId);
    }



}