package com.Ecommerce.ECommerceApp.entity;

import com.Ecommerce.ECommerceApp.Enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> items;

    private double totalPrice;

    private String shippingAddress;

    private String paymentStatus;

}
