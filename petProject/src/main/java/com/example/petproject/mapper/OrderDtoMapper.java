package com.example.petproject.mapper;

import com.example.petproject.dto.OrderDTO;
import com.example.petproject.entity.Order;
import com.example.petproject.entity.User;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrderDtoMapper extends BaseDtoMapper {

    private final CartDtoMapper cartDtoMapper;

    public OrderDtoMapper(ModelMapper modelMapper, CartDtoMapper cartDtoMapper) {
        super(modelMapper);
        this.cartDtoMapper = cartDtoMapper;
    }

    public OrderDTO toOrderDTO(Order order) {
        OrderDTO orderDTO = toDTO(order, OrderDTO.class);
        orderDTO.setCart(cartDtoMapper.toCartDTO(order.getCart()));
        orderDTO.setUserId(order.getUser().getId());
        return orderDTO;
    }

    public Order toOrderEntity(OrderDTO orderDTO) {
        Order order = toEntity(orderDTO, Order.class);
        order.setSum(orderDTO.getCart().getSum());
        order.setUser(User.builder().id(orderDTO.getUserId()).build());
        return order;
    }
}
