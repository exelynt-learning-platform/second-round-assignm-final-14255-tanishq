package com.Ecommerce.ECommerceApp.service;

import com.Ecommerce.ECommerceApp.entity.*;
import com.Ecommerce.ECommerceApp.repository.CartItemRepository;
import com.Ecommerce.ECommerceApp.repository.CartRepository;
import com.Ecommerce.ECommerceApp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;


    public Cart getCart(User user) {
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart cart = Cart.builder().user(user).items(new ArrayList<>()).build();
            return cartRepo.save(cart);
        });
    }


    public Cart addProduct(User user, Long productId, int quantity) {
        Cart cart = getCart(user);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem item = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .cart(cart)
                    .build();
            cart.getItems().add(item);
        }

        return cartRepo.save(cart);
    }

    public Cart updateProduct(User user, Long productId, int quantity) {
        Cart cart = getCart(user);

        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(quantity));

        return cartRepo.save(cart);
    }


    public Cart removeProduct(User user, Long productId) {
        Cart cart = getCart(user);

        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));

        return cartRepo.save(cart);
    }

    public Cart getCartByUser(User user) {
        return cartRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + user.getEmail()));
    }

    public Cart getCartByUserEmail(String email) {
        return cartRepo.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + email));
    }


    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}