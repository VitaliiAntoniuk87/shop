package com.example.petproject.controller;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
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
        return cartService.saveCart(cartDTO);
    }

    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable long cartId) {
        cartService.deleteCart(cartId);
    }

    @PostMapping("/{cartId}/product/{productId}")
    public CartDTO addProductToCart(@PathVariable long cartId, @PathVariable long productId, @Param("quantity") int quantity) {
        return cartService.addProductToCart(cartId, productId, quantity);
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public CartDTO removeProductFromCart(@PathVariable long cartId, @PathVariable long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return cartService.getCartById(cartId);
    }

    @PutMapping("/{cartId}/product/{productId}")
    public CartDTO updateProductInCart(@PathVariable long cartId, @PathVariable long productId, @Param("quantity") int quantity) {
        cartService.updateProductInCart(cartId, productId, quantity);
        return cartService.getCartById(cartId);
    }
}
