package com.example.petproject.controller;

import com.example.petproject.dto.OrderDTO;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/list/{userId}")
    public List<OrderDTO> getOrderListByUserId(@PathVariable long userId) {

    }
}
