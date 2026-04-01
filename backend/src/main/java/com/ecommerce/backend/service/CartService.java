package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    // 🔐 Get logged-in user
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Cart addToCart(@NonNull Long productId, int qty) {

        User user = getCurrentUser();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🚨 Stock validation
        if (product.getStock() < qty) {
            throw new RuntimeException("Not enough stock available");
        }

        // Get or create cart
        Cart cart = getOrCreateCart(user);

        // 🔁 Check if product already exists
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {

            int newQty = existingItem.getQuantity() + qty;

            if (product.getStock() < newQty) {
                throw new RuntimeException("Exceeds available stock");
            }

            existingItem.setQuantity(newQty);

        } else {

            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(qty);
            item.setCart(cart);

            cart.getItems().add(item);
        }

        return cartRepo.save(cart); // ✅ correct placement
    }

    // ✅ Get logged-in user's cart
    public Cart getUserCart() {
        User user = getCurrentUser();
        return getOrCreateCart(user);
    }

    private Cart getOrCreateCart(User user) {
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepo.save(newCart);
        });

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
            cart = cartRepo.save(cart);
        }

        return cart;
    }
}