package com.ecommerce.backend.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long userId;
    private String shippingAddress;
}