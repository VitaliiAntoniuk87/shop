package com.example.petproject.service;

import com.example.petproject.dto.OrderDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.Order;
import com.example.petproject.entity.enums.CartStatus;
import com.example.petproject.exception.ObjectNotFoundException;
import com.example.petproject.mapper.CartDtoMapper;
import com.example.petproject.mapper.OrderDtoMapper;
import com.example.petproject.repository.CartRepository;
import com.example.petproject.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
@Builder
@AllArgsConstructor
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderDtoMapper;
    private final CartDtoMapper cartDtoMapper;
    private final CartService cartService;
    private final CartRepository cartRepository;

    public List<OrderDTO> getOrdersByUserId(long id) {
        List<Order> orders = orderRepository.findAllByUserId(id);
        if (!orders.isEmpty()) {
            return orderDtoMapper.toOrderDTOList(orders);
        } else {
            log.info("No orders found for UserId = " + id);
            throw new ObjectNotFoundException("No orders found for UserId = " + id);
        }
    }

    public OrderDTO getOrderById(long id) {
        Order order = orderRepository.findOrderById(id);
        if (order != null) {
            return orderDtoMapper.toOrderDTO(order);
        } else {
            log.info("No orders found by Id = " + id);
            throw new ObjectNotFoundException("No orders found by Id = " + id);
        }
    }

    @Transactional
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        Cart cart = cartRepository.findByUserIdAndStatusAndOrder(orderDTO.getUserId(), CartStatus.NEW, null);
        if (cart != null) {
            Order order = orderDtoMapper.toOrderEntity(orderDTO);
            order.setCart(cart);
            order.setSum(cart.getSum());
            order.setCreateDate(LocalDateTime.now());
            orderRepository.save(order);
            cart.setOrder(order);
            cartRepository.updateCartOrderByCartId(cart.getId(), order.getId());
            return orderDtoMapper.toOrderDTO(order);
        } else {
            log.warn("Active cart is not found for this userId = " + orderDTO.getUserId());
            throw new ObjectNotFoundException("Active cart not found");
        }
    }


}
