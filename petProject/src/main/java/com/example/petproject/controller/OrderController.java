package com.example.petproject.controller;

import com.example.petproject.dto.OrderDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {


    @GetMapping("/list/{id}")
    public List<OrderDTO> getAllByUserId(@PathVariable long id) {

    }

    @PostMapping
    public OrderDTO saveOrder(@RequestBody OrderDTO orderDTO) {

    }
}
