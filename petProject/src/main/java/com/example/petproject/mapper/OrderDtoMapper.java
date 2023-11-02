package com.example.petproject.mapper;

import com.example.petproject.dto.OrderDTO;
import com.example.petproject.entity.Order;
import com.example.petproject.entity.User;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class OrderDtoMapper extends BaseDtoMapper {

    private final CartDtoMapper cartDtoMapper;

    public OrderDtoMapper(ModelMapper modelMapper, CartDtoMapper cartDtoMapper) {
        super(modelMapper);
        this.cartDtoMapper = cartDtoMapper;
    }

    public OrderDTO toOrderDTO(Order entity) {
        OrderDTO orderDTO = toDTO(entity, OrderDTO.class);
        orderDTO.setCart(cartDtoMapper.toCartDTO(entity.getCart()));
        orderDTO.setUserId(entity.getUser().getId());
        return orderDTO;
    }

    public List<OrderDTO> toOrderDTOList(List<Order> entities) {
        return entities.stream()
                .map(this::toOrderDTO)
                .toList();
    }

    public Order toOrderEntity(OrderDTO orderDTO) {
        Order order = toEntity(orderDTO, Order.class);
        order.setUser(User.builder().id(orderDTO.getUserId()).build());
        return order;
    }

    public List<Order> toOrderEntityList(List<OrderDTO> orderDTOS) {
        return orderDTOS.stream()
                .map(this::toOrderEntity)
                .toList();
    }
}
