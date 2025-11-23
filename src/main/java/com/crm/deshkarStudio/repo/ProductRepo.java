package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Products, Long> {

    List<Products> findByIsActive(Boolean active);
}
