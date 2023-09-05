package com.example.petproject.mapper;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.CartStatus;
import com.example.petproject.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartDtoMapper extends BaseDtoMapper {

    private final ProductCartDtoMapper productCartDtoMapper;

    public CartDtoMapper(ModelMapper modelMapper, ProductCartDtoMapper productCartDtoMapper) {
        super(modelMapper);
        this.productCartDtoMapper = productCartDtoMapper;
    }

    public CartDTO toCartDTO(Cart cart) {
        CartDTO cartDTO = toDTO(cart, CartDTO.class);
        cartDTO.setProducts(productCartDtoMapper.toProductCartDTOList(cart.getProducts()));
        cartDTO.setUserId(cart.getUser().getId());
        return cartDTO;
    }

    public Cart toCartEntity(CartDTO cartDTO) {
        Cart cart = toEntity(cartDTO, Cart.class);
        cart.setUser(User.builder().id(cartDTO.getUserId()).build());
        cart.setStatus(CartStatus.NEW);
        cart.setSum(roundToHundredths(cartDTO.getProducts().stream().mapToDouble(ProductCartDTO::getTotal).sum()));
        cart.setProducts(productCartDtoMapper.toProductCartEntityList(cartDTO.getProducts()));
        return cart;
    }

    private double roundToHundredths(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
