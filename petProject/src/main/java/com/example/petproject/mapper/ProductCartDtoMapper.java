package com.example.petproject.mapper;

import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.Product;
import com.example.petproject.entity.ProductCart;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
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
        List<ProductCartDTO> productCartDTOS = entities.stream()
                .map(this::toProductCartDTO)
                .toList();
        log.info("Entities ProductCart List was transferred to DTOs ProductCart List");
        return productCartDTOS;
    }

    public ProductCart toProductCartEntity(ProductCartDTO productCartDTO) {
        ProductCart productCart = toEntity(productCartDTO, ProductCart.class);
        productCart.setProduct(Product.builder().id(productCartDTO.getProductId()).build());
        productCart.setTotal(productCartDTO.getPrice() * productCartDTO.getQuantity());
        return productCart;
    }

    public List<ProductCart> toProductCartEntityList(List<ProductCartDTO> productCartDTOS) {
        List<ProductCart> productCarts = productCartDTOS.stream()
                .map(this::toProductCartEntity)
                .toList();
        log.info("DTOs ProductCart List was transferred to Entities ProductCart List");
        return productCarts;
    }
}
