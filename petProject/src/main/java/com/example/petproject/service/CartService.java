package com.example.petproject.service;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.CartStatus;
import com.example.petproject.mapper.CartDtoMapper;
import com.example.petproject.repository.CartRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@Builder
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartDtoMapper cartDtoMapper;

    public CartDTO getCartByUserId(long id) {
        Cart cart = cartRepository.findByUserIdAndStatus(id, CartStatus.NEW);
        return cartDtoMapper.toCartDTO(cart);
    }

    public CartDTO saveCart(CartDTO cartDTO) {
        if (productService.isProductsPriceAndQuantityCorrect(cartDTO)) {
            Cart cart = cartDtoMapper.toCartEntity(cartDTO);
            Cart savedCart = cartRepository.save(cart);
            long generatedId = savedCart.getId();

        }
    }


}
