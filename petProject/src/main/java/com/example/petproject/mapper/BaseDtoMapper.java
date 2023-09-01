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

    public <E, D> List<D> toDTOList(List<E> entity, Class<D> dtoClass) {
        return entity.stream().map(e -> toDTO(e, dtoClass)).toList();
    }

    public <E, D> List<E> toEntityList(List<D> dto, Class<E> entityClass) {
        return dto.stream().map(d -> toDTO(d, entityClass)).toList();
    }


}
