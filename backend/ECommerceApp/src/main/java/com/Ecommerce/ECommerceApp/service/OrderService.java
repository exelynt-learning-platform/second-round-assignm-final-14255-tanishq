package com.Ecommerce.ECommerceApp.service;

import com.Ecommerce.ECommerceApp.Enums.PaymentStatus;
import com.Ecommerce.ECommerceApp.entity.*;
import com.Ecommerce.ECommerceApp.repository.OrderRepository;
import com.Ecommerce.ECommerceApp.repository.ProductRepository;
import com.Ecommerce.ECommerceApp.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

//    private final CartService cartService;

    private final ProductRepository productRepository;


    public Order createOrder(User user, OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice() * item.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        double totalPrice = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));

        return orderRepository.save(order);
    }

    public Order updateOrderByUser(User user, Long orderId, String newShippingAddress) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        if (!"PAID".equals(order.getPaymentStatus())) {
            order.setShippingAddress(newShippingAddress);
        } else {
            throw new RuntimeException("Cannot update shipped/paid orders");
        }
        return orderRepository.save(order);
    }


    public List<Order> getOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public Order getOrderById(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        return order;
    }


    }
