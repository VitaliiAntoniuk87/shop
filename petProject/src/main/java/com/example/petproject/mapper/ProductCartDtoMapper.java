package com.example.petproject.mapper;

import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.Product;
import com.example.petproject.entity.ProductCart;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCartDtoMapper extends BaseDtoMapper {

    public ProductCartDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    public ProductCartDTO toProductCartDTO(ProductCart entity) {
        ProductCartDTO productCartDTO = toDTO(entity, ProductCartDTO.class);
        productCartDTO.setProductId(entity.getProduct().getId());
        return productCartDTO;
    }

    public List<ProductCartDTO> toProductCartDTOList(List<ProductCart> entities) {
        return entities.stream()
                .map(this::toProductCartDTO)
                .collect(Collectors.toList());
    }

    public ProductCart toProductCartEntity(ProductCartDTO productCartDTO) {
        ProductCart productCart = toEntity(productCartDTO, ProductCart.class);
        productCart.setProduct(Product.builder().id(productCartDTO.getProductId()).build());
        return productCart;
    }

    public List<ProductCart> toProductCartEntityList(List<ProductCartDTO> productCartDTOS) {
        return productCartDTOS.stream()
                .map(this::toProductCartEntity)
                .toList();
    }
}
