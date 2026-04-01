package com.Ecommerce.ECommerceApp.controller;


import com.Ecommerce.ECommerceApp.entity.Product;
import com.Ecommerce.ECommerceApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public Product create(@RequestBody Product p) {
        return productService.createProduct(p);
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/getProduct/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/updateProduct/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product p) {
        return productService.updateProduct(id, p);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}