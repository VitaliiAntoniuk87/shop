package com.example.petproject.service;

import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.dto.ProductDTO;
import com.example.petproject.entity.Product;
import com.example.petproject.entity.ProductCart;
import com.example.petproject.mapper.BaseDtoMapper;
import com.example.petproject.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Builder
@AllArgsConstructor
@Log4j2
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

    public int decrementProductQuantity(List<ProductCartDTO> products) {
        int counter = 0;
        for (ProductCartDTO product : products) {
            counter += productRepository.updateQuantity(product.getProductId(), -1 * product.getQuantity());
            log.info("Product with id = " + product.getProductId() + " has been decremented by " + product.getQuantity() + " units");
        }
        if (products.size() != counter) {
            log.error("Some product's quantity wasn't updated");
        }
        return counter;
    }

    public int decrementProductQuantity(long productId, int quantity) {
        return productRepository.updateQuantity(productId, -1 * quantity);
    }


    public int decrementProductQuantityWithEntity(List<ProductCart> products) {
        int counter = 0;
        for (ProductCart product : products) {
            counter += productRepository.updateQuantity(product.getProduct().getId(), -1 * product.getQuantity());
        }
        return counter;
    }


    public int incrementQuantity(List<ProductCartDTO> products) {
        int counter = 0;
        for (ProductCartDTO product : products) {
            counter += productRepository.updateQuantity(product.getProductId(), product.getQuantity());
        }
        return counter;
    }

    public int incrementQuantity(ProductCartDTO product) {
        return productRepository.updateQuantity(product.getProductId(), product.getQuantity());
    }

    public int incrementQuantity(long productId, int quantity) {
        return productRepository.updateQuantity(productId, quantity);
    }

    public int incrementProductQuantityWithEntity(List<ProductCart> products) {
        int counter = 0;
        for (ProductCart product : products) {
            counter += productRepository.updateQuantity(product.getProduct().getId(), product.getQuantity());
        }
        return counter;
    }

    public int incrementProductQuantityWithEntity(ProductCart product) {
        return productRepository.updateQuantity(product.getProduct().getId(), product.getQuantity());
    }


}
