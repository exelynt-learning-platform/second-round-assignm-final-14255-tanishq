package com.Ecommerce.ECommerceApp.controller;

import com.Ecommerce.ECommerceApp.entity.Order;
import com.Ecommerce.ECommerceApp.entity.User;
import com.Ecommerce.ECommerceApp.request.OrderRequest;
import com.Ecommerce.ECommerceApp.service.AuthService;
import com.Ecommerce.ECommerceApp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;


    @PostMapping("/create")
    public Order createOrder(Authentication auth,
                             @RequestBody OrderRequest orderRequest) {
        User user = authService.getUserByEmail(auth.getName());
        return orderService.createOrder(user, orderRequest);
    }

    @PutMapping("/update/{orderId}")
    public Order updateOrder(Authentication auth,
                             @PathVariable Long orderId,
                             @RequestBody OrderRequest orderRequest) {
        User user = authService.getUserByEmail(auth.getName());
        return orderService.updateOrderByUser(user, orderId, orderRequest.getShippingAddress());
    }


    @GetMapping("/getOrders")
    public List<Order> getOrders(Authentication auth) {
        User user = authService.getUserByEmail(auth.getName());
        return orderService.getOrders(user);
    }


    @GetMapping("/getOrder/{id}")
    public Order getOrder(Authentication auth, @PathVariable Long id) {
        User user = authService.getUserByEmail(auth.getName());
        return orderService.getOrderById(user, id);
    }
}
