package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Products, Long> {
}
