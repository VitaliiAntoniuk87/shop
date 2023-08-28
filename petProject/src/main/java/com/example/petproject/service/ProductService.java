package com.example.petproject.service;

import com.example.petproject.dto.ProductDTO;
import com.example.petproject.entity.Product;
import com.example.petproject.mapper.BaseDtoMapper;
import com.example.petproject.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Builder
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BaseDtoMapper baseDtoMapper;


    public List<ProductDTO> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> baseDtoMapper.toDTO(product, ProductDTO.class)).toList();
    }

    public List<ProductDTO> getAllByCategory(long categoryId) {
        List<Product> products = productRepository.findProductsByCategoryId(categoryId);
        return products.stream()
                .map(product -> baseDtoMapper.toDTO(product, ProductDTO.class)).toList();
    }
}
