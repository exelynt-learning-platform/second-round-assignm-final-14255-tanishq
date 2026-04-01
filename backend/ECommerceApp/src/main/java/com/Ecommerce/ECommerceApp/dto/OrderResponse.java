package com.Ecommerce.ECommerceApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String shippingAddress;
    private double totalPrice;
    private List<OrderItemResponse> items;

    @Data
    public static class OrderItemResponse {
        private String productName;
        private int quantity;
        private double price;
    }
}
