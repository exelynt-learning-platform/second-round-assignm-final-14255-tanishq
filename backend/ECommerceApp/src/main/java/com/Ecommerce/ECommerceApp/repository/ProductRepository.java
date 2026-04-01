package com.Ecommerce.ECommerceApp.repository;

import com.Ecommerce.ECommerceApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


}
