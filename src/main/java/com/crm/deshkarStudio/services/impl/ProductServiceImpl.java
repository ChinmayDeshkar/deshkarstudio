package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.Products;
import com.crm.deshkarStudio.repo.ProductRepo;
import com.crm.deshkarStudio.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Override
    public List<Products> getAllProducts() {
        return productRepo.findAll();
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
}
