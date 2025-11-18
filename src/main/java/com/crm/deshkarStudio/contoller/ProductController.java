package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.model.Products;
import com.crm.deshkarStudio.repo.ProductRepo;
import com.crm.deshkarStudio.services.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public List<Products> getAll(){
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public Products addProduct(@RequestBody Products product){
        log.info(product.toString());
        return productService.saveProduct(product);
    }

    @PostMapping("/add-all")
    public Products addAll(@RequestBody List<Products> products){
//        log.info(product.toString());
         productService.addAll(products);
         return null;
    }

}
