package com.Ecommerce.ECommerceApp.repository;

import com.Ecommerce.ECommerceApp.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
