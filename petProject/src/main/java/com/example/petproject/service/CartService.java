package com.example.petproject.service;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.CartStatus;
import com.example.petproject.entity.Product;
import com.example.petproject.mapper.BaseDtoMapper;
import com.example.petproject.repository.CartRepository;
import com.example.petproject.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@Builder
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final BaseDtoMapper baseDtoMapper;

    public CartDTO getCartByUserId(long id) {
        Cart cart = cartRepository.findByUserIdAndStatus(id, CartStatus.NEW);
        return baseDtoMapper.toCartDTO(cart);
    }

//    public Cart addToCart(long productId, int quantity, long userId) {
//        Product product = productRepository.findProductById(productId);
//        if (quantity <= product.getQuantity()) {
//            Cart.builder()
//                    .product(product)
//                    .quantity(quantity)
//                    .price(product.getCurrentPrice())
//                    .createDate(LocalDateTime.now())
//                    .status(CartStatus.NEW)
//                    .
//        }
//    }

//    private boolean isProductPriceAndQuantityCorrect(List<CartDTO> cartDTOS) {
//
//        Product product = productRepository.findProductById(productId);
//
//    }
}
