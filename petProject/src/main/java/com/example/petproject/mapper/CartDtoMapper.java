package com.example.petproject.mapper;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.enums.CartStatus;
import com.example.petproject.entity.User;
import com.example.petproject.service.MathServices;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
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
        log.info("Entity Cart was transferred to DTO Cart");
        return cartDTO;
    }

    public Cart toCartEntity(CartDTO cartDTO) {
        Cart cart = toEntity(cartDTO, Cart.class);
        cart.setUser(User.builder().id(cartDTO.getUserId()).build());
        cart.setStatus(CartStatus.NEW);
        cart.setSum(MathServices.roundToHundredths(cartDTO.getProducts().stream().mapToDouble(ProductCartDTO::getTotal).sum()));
        cart.setProducts(productCartDtoMapper.toProductCartEntityList(cartDTO.getProducts()));
        log.info("DTO Cart was transferred to Entity Cart");
        return cart;
    }

}
