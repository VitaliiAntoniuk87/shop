package com.example.petproject.controller;

import com.example.petproject.dto.ProductDTO;
import com.example.petproject.entity.Product;
import com.example.petproject.repository.ProductRepository;
import com.example.petproject.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private ProductRepository productRepository;

    @GetMapping
    public List<ProductDTO> getProducts() {
         return productService.getAll();
    }

    @GetMapping("/category/{id}")
    public List<ProductDTO> getProductsByCategory(@PathVariable(value = "id") long id) {
        return productService.getAllByCategory(id);
    }


}


