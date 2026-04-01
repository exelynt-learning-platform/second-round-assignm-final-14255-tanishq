package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    // 🔐 Get logged-in user
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public Order createOrder(String address) {

        User user = getCurrentUser();

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {

            Product product = item.getProduct();

            // 🚨 Stock validation
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException(product.getName() + " is out of stock");
            }

            // Reduce stock
            product.setStock(product.getStock() - item.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItems.add(orderItem);

            total += orderItem.getPriceAtPurchase() * orderItem.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(total);
        order.setShippingAddress(address);
        order.setItems(orderItems);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        // ✅ Use Enum
        order.setStatus(OrderStatus.CREATED);

        Order savedOrder = orderRepo.save(order);

        // 🧹 Clear cart
        cart.getItems().clear();
        cartRepo.save(cart);

        return savedOrder;
    }
}