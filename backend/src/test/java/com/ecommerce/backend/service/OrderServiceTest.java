package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;

    private User testUser;

    @BeforeEach
    void setup() {

        // 🔥 Clean database (VERY IMPORTANT)
        orderRepo.deleteAll();
        cartRepo.deleteAll();
        userRepo.deleteAll();
        productRepo.deleteAll();

        // ✅ Create User
        testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword("123");
        testUser.setRole(Role.USER);
        testUser = userRepo.save(testUser);

        // ✅ Create Product
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(50000);
        product.setStock(10);
        product = productRepo.save(product);

        // ✅ Create Cart
        Cart cart = new Cart();
        cart.setUser(testUser);
        cart.setItems(new ArrayList<>());
        cart = cartRepo.save(cart);

        // ✅ Create CartItem
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(1);

        cart.getItems().add(item);
        cartRepo.save(cart);
    }

    @Test
    void testCreateOrder() {

        // ✅ Call service
        Order order = orderService.createOrder(testUser.getId());

        // ✅ Assertions
        assertNotNull(order);
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertTrue(order.getTotalPrice() > 0);
    }
}