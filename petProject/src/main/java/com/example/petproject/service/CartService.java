package com.example.petproject.service;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.CartStatus;
import com.example.petproject.entity.ProductCart;
import com.example.petproject.mapper.CartDtoMapper;
import com.example.petproject.mapper.ProductCartDtoMapper;
import com.example.petproject.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

@Service
@Data
@AllArgsConstructor
@Builder
public class CartService {

    private final CartDtoMapper cartDtoMapper;
    private final ProductCartDtoMapper productCartDtoMapper;
    private final ProductService productService;
    private final ProductCartService productCartService;
    private final CartRepository cartRepository;

    public CartDTO getCartByUserId(long id) {
        Cart cart = cartRepository.findByUserIdAndStatus(id, CartStatus.NEW);
        return cartDtoMapper.toCartDTO(cart);
    }

    @Transactional
    public CartDTO saveCart(CartDTO cartDTO) {
        if (productService.isProductsPriceAndQuantityCorrect(cartDTO)) {
            Cart cart = cartDtoMapper.toCartEntity(cartDTO);
            cart.setCreateDate(LocalDateTime.now());
            Cart savedCart = cartRepository.save(cart);

            List<ProductCart> productCart = productCartDtoMapper.toProductCartEntityList(cartDTO.getProducts());
            productCart.forEach(pc -> pc.setCart(savedCart));
            productCartService.saveAll(productCart);

            cartDTO.setId(savedCart.getId());
            cartDTO.setCreateDate(LocalDateTime.now());
            productService.reduceQuantity(cartDTO.getProducts());
        }
        return cartDTO;
    }

    @Transactional
    public int deleteCart(long userId) {
        return cartRepository.deleteByUserIdAndStatus(userId, CartStatus.NEW);
    }


}
