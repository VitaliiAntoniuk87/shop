package com.example.petproject.controller;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public CartDTO getCartByUserId(@PathVariable long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping
    public CartDTO saveCart(@RequestBody CartDTO cartDTO) {

    }
}
