package com.example.petproject.controller;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public CartDTO getCartByUserId(@PathVariable long userId) {
        return cartService.getCartByUserId(userId);
    }

}
