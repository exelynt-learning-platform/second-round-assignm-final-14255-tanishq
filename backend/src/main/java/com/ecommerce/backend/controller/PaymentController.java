package com.ecommerce.backend.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/payment")
@Validated
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public String createPayment(@PathVariable @NotNull @NonNull Long orderId) throws Exception {
        return paymentService.createPayment(orderId);
    }

    @PostMapping("/callback/{orderId}")
    public String callback(@PathVariable @NotNull @NonNull Long orderId,
                           @RequestParam @NotNull Boolean success) {

        paymentService.updatePaymentStatus(orderId, success);
        return "Updated";
    }
}