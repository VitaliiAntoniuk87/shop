package com.example.petproject.mapper;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.dto.ProductDTO;
import com.example.petproject.dto.PublicUserDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.ProductCart;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BaseDtoMapper {

    private final ModelMapper modelMapper;

    public <E, D> D toDTO(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <E, D> E toEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public CartDTO toCartDTO(Cart cart) {
        CartDTO cartDTO = toDTO(cart, CartDTO.class);
        cartDTO.setProducts(toProductCartDTOList(cart.getProducts()));
        cartDTO.setUser(toDTO(cart.getUser(), PublicUserDTO.class));
        return cartDTO;
    }

    public ProductCartDTO toProductCartDTO(ProductCart entity) {
        ProductCartDTO productCartDTO = toDTO(entity, ProductCartDTO.class);
        productCartDTO.setProduct(toDTO(entity.getProduct(), ProductDTO.class));
        return productCartDTO;
    }

    public List<ProductCartDTO> toProductCartDTOList(List<ProductCart> entities) {
        return entities.stream()
                .map(this::toProductCartDTO)
                .collect(Collectors.toList());
    }


}
