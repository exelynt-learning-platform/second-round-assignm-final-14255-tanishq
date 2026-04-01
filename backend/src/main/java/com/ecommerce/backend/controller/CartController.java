package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.CartItemResponse;
import com.ecommerce.backend.dto.CartResponse;
import com.ecommerce.backend.dto.ProductResponse;
import com.ecommerce.backend.dto.UserResponse;
import com.ecommerce.backend.entity.Cart;
import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.service.CartService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
@Validated
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PostMapping("/add")
    public CartResponse add(@RequestParam @NotNull Long productId,
                            @RequestParam @Min(1) int qty) {

        return toCartResponse(service.addToCart(productId, qty)); // ✅ no userId
    }

    // ✅ Optional: get current user's cart
    @GetMapping
    public CartResponse getCart() {
        return toCartResponse(service.getUserCart());
    }

    private CartResponse toCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUser(toUserResponse(cart.getUser()));

        List<CartItemResponse> itemResponses = new ArrayList<>();
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                CartItemResponse itemResponse = new CartItemResponse();
                itemResponse.setId(item.getId());
                itemResponse.setProduct(toProductResponse(item.getProduct()));
                itemResponse.setQuantity(item.getQuantity());
                itemResponses.add(itemResponse);
            }
        }
        response.setItems(itemResponses);
        return response;
    }

    private UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return response;
    }

    private ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
}