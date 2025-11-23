package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.Products;

import java.util.List;

public interface ProductService {

    List<Products> getActiveProducts();

    Products saveProduct(Products product);

    Products getById(long id);

    void addAll(List<Products> products);

    Products updateProductById(long id, Products product);

    void deleteProductById(long id);
}
