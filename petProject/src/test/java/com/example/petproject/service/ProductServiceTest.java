package com.example.petproject.service;

import com.example.petproject.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductServiceTest {

    private final ProductService productService;


    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }


//    @Test
//    void getAllTest() {
//        List<Product> products = productService.getAll();
//        Assertions.assertEquals(4, products.size());
//
//        System.out.println(products);
//    }

//    @Test
//    void getAllByCategoryIdTest() {
//        long categoryId = 1;
//        List<Product> products = productService.getAllByCategory(categoryId);
//        Assertions.assertEquals(3, products.size());
//
//        System.out.println(products);
//    }
}