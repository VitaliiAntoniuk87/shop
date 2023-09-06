package com.example.petproject.service;

import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.entity.ProductCart;
import com.example.petproject.repository.ProductCartRepository;
import com.example.petproject.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class ProductCartService {

    private final ProductCartRepository productCartRepository;

    public List<ProductCart> saveAll(List<ProductCart> productCart) {
        return productCartRepository.saveAll(productCart);
    }

    public ProductCart save(ProductCart productCart) {
        return productCartRepository.save(productCart);
    }

}