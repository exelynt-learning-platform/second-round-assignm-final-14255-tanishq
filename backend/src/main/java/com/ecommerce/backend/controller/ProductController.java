package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductResponse;
import com.ecommerce.backend.entity.Product;
import jakarta.validation.Valid;
import com.ecommerce.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ProductResponse create(@Valid @RequestBody Product p) {
        return toProductResponse(service.create(p));
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        List<ProductResponse> response = new ArrayList<>();
        for (Product product : service.getAll()) {
            response.add(toProductResponse(product));
        }
        return response;
    }

    private ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
}