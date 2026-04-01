package com.Ecommerce.ECommerceApp.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private String shippingAddress;


    private List<Item> items;

    @Data
    public static class Item {
        private Long productId;
        private int quantity;
    }
}
