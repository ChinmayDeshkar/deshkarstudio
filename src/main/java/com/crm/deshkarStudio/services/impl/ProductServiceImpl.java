package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.Products;
import com.crm.deshkarStudio.repo.ProductRepo;
import com.crm.deshkarStudio.services.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Override
    public List<Products> getActiveProducts() {
        return productRepo.findByIsActive(true);
    }

    @Override
    public Products saveProduct(Products product) {
        return productRepo.save(product);
    }

    @Override
    public Products getById(long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("No Product found!!"));
    }

    @Override
    public void addAll(List<Products> products) {
        productRepo.saveAll(products);
    }

    @Override
    public Products updateProductById(long id, Products product) {
        Products existing = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Error finding Product by id: " + id));
        log.debug("Found existing product: " + existing);
        product.setDte_updated(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        log.debug(product.toString());
        return productRepo.save(product);
    }

    @Override
    public void deleteProductById(long id) {
        log.debug("Deactivating product with id: " + id);
        Products p = productRepo.findById(id).orElseThrow();
        p.setActive(false);   // Softly Delete
        productRepo.save(p);
    }
}
