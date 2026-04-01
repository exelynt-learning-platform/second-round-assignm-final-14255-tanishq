package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderStatus;
import com.ecommerce.backend.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.key:dummy}")
    private String key;

    // ✅ Create payment for ORDER (not random amount)
    public String createPayment(@NonNull Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        validateOrderOwnership(order);

        if ("dummy".equals(key)) {
            return "mock_client_secret_" + orderId;
        }

        Stripe.apiKey = key;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) (order.getTotalPrice() * 100));
        params.put("currency", "inr");

        PaymentIntent intent = PaymentIntent.create(params);

        return intent.getClientSecret(); // ✅ important
    }

    // ✅ Update order after payment
    public void updatePaymentStatus(@NonNull Long orderId, boolean success) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        validateOrderOwnership(order);

        if (success) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }

        orderRepository.save(order);
        
        
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public OrderRepository getOrderRepository() {
		return orderRepository;
	}

    private void validateOrderOwnership(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) {
            return;
        }

        String currentUsername = authentication.getName();
        if (order.getUser() == null || !currentUsername.equals(order.getUser().getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access another user's order");
        }
    }
}