package com.example.petproject.service;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.dto.ProductDTO;
import com.example.petproject.entity.Product;
import com.example.petproject.entity.ProductCart;
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

    public boolean isProductsPriceAndQuantityCorrect(List<ProductCartDTO> productCartDTO) {
        List<Long> productIds = productCartDTO.stream().mapToLong(ProductCartDTO::getProductId).boxed().toList();
        List<Product> productsFromDatabase = productRepository.findAllByIdIn(productIds);

        for (ProductCartDTO element : productCartDTO) {

            long productId = element.getProductId();
            int quantity = element.getQuantity();
            double price = element.getPrice();

            Product productFromDatabase = productsFromDatabase.stream()
                    .filter(product -> product.getId() == productId)
                    .findFirst()
                    .orElse(null);

            if (productFromDatabase == null) {
                return false;
            }
            if (productFromDatabase.getCurrentPrice() != price ||
                    productFromDatabase.getQuantity() < quantity) {
                return false;
            }

        }
        return true;

    }

    public int reduceProductQuantity(List<ProductCartDTO> products) {
        int counter = 0;
        for (ProductCartDTO product : products) {
            counter += productRepository.updateQuantity(product.getProductId(), -1 * product.getQuantity());
        }
        return counter;
    }

    public int reduceProductQuantity(long productId, int quantity) {
        return productRepository.updateQuantity(productId, -1 * quantity);
    }


    public int reduceProductQuantityWithEntity(List<ProductCart> products) {
        int counter = 0;
        for (ProductCart product : products) {
            counter += productRepository.updateQuantity(product.getProduct().getId(), -1 * product.getQuantity());
        }
        return counter;
    }


    public int increaseQuantity(List<ProductCartDTO> products) {
        int counter = 0;
        for (ProductCartDTO product : products) {
            counter += productRepository.updateQuantity(product.getProductId(), product.getQuantity());
        }
        return counter;
    }

    public int increaseQuantity(ProductCartDTO product) {
        return productRepository.updateQuantity(product.getProductId(), product.getQuantity());
    }

    public int increaseQuantity(long productId, int quantity) {
        return productRepository.updateQuantity(productId, quantity);
    }

    public int increaseProductQuantityWithEntity(List<ProductCart> products) {
        int counter = 0;
        for (ProductCart product : products) {
            counter += productRepository.updateQuantity(product.getProduct().getId(), product.getQuantity());
        }
        return counter;
    }

    public int increaseProductQuantityWithEntity(ProductCart product) {
        return productRepository.updateQuantity(product.getProduct().getId(), product.getQuantity());
    }


}
