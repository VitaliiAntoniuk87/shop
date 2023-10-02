package com.example.petproject.service;

import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.entity.ProductCart;
import com.example.petproject.repository.ProductCartRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
@Log4j2
public class ProductCartService {

    private final ProductCartRepository productCartRepository;

    public List<ProductCart> saveAll(List<ProductCart> productCart) {
        return productCartRepository.saveAll(productCart);
    }

    public ProductCart save(ProductCart productCart) {
        return productCartRepository.save(productCart);
    }

    public int deleteAllByCartIdAndProductId(long cartId, long productId) {
        return productCartRepository.deleteAllByCartIdAndProductId(cartId, productId);
    }

    public int updateProductCartQuantityTotal(ProductCart productCart) {
        return productCartRepository.updateProductCartQuantityTotal(
                productCart.getQuantity(), productCart.getTotal(),
                productCart.getProduct().getId(), productCart.getCart().getId());
    }

    public int updateProductCartQuantityTotalByDifference(long cartId, List<ProductCartDTO> productCartDTOS) {
        int counter = 0;
        for (ProductCartDTO productCartDTO : productCartDTOS) {
            counter += productCartRepository.updateProductCartQuantityTotalByDifference(
                    productCartDTO.getQuantity(), productCartDTO.getTotal(), productCartDTO.getProductId(), cartId
            );
        }
        if (productCartDTOS.size() != counter) {
            log.error("Some product's quantity wasn't updated");
        }
        return counter;
    }

}
