package com.example.petproject.service;

import com.example.petproject.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
@Builder
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;


}
