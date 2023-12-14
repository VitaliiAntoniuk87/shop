package com.example.petproject.controller;

import com.example.petproject.dto.OrderDTO;
import com.example.petproject.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/list/user/{id}")
    public List<OrderDTO> getAllByUserId(@PathVariable long id) {
        return orderService.getOrdersByUserId(id);
    }

    @PostMapping
    public OrderDTO saveOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.saveOrder(orderDTO);
    }

    @GetMapping("/details/{id}")
    public OrderDTO getOrderById(@PathVariable long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/details/{id}")
    public OrderDTO updateOrder(@PathVariable long id, @RequestBody OrderDTO orderDTO) {
        if (id == orderDTO.getId()) {
            return orderService.updateOrder(orderDTO);
        }
        return null;
        //todo exception
    }

    @GetMapping("/delivery")
    public List<String> getAllowedDeliveryMethods() {

    }


}
