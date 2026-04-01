package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.OrderItemResponse;
import com.ecommerce.backend.dto.OrderResponse;
import com.ecommerce.backend.dto.ProductResponse;
import com.ecommerce.backend.dto.UserResponse;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.service.OrderService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public OrderResponse create(@RequestParam @NotBlank String address) {
        return toOrderResponse(service.createOrder(address)); // ✅ no userId
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUser(toUserResponse(order.getUser()));
        response.setTotalPrice(order.getTotalPrice());
        response.setShippingAddress(order.getShippingAddress());
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : null);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setId(item.getId());
                itemResponse.setProduct(toProductResponse(item.getProduct()));
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());
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