package com.Ecommerce.ECommerceApp.service;

import com.Ecommerce.ECommerceApp.entity.Product;
import com.Ecommerce.ECommerceApp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository repo;

    public Product createProduct(Product p) {
        return repo.save(p);
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product p) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        existing.setName(p.getName());
        existing.setPrice(p.getPrice());
        existing.setStock(p.getStock());

        return repo.save(existing);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }


}
