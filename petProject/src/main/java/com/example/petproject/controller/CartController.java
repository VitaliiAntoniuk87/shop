package com.example.petproject.controller;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.mapper.CartDtoMapper;
import com.example.petproject.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartDtoMapper cartDtoMapper;

    @GetMapping("/{userId}")
    public CartDTO getCartByUserId(@PathVariable long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping
    public CartDTO saveCart(@RequestBody CartDTO cartDTO) {
        System.out.println(cartDTO); //delete after tests
        Cart cart = cartDtoMapper.toCartEntity(cartDTO); //delete after tests
        System.out.println(cart); //delete after tests
        return cartService.saveCart(cartDTO);
    }

    @DeleteMapping
    public int deleteCart(@RequestBody CartDTO cartDTO) {
        return cartService.deleteCart(cartDTO);
    }

    @PostMapping("/{cartId}/product/{productId}")
    public CartDTO addProductToCart(@PathVariable long cartId, @PathVariable long productId, @Param("quantity") int quantity) {
        return cartService.addProductToCart(cartId, productId, quantity);
    }

//    @DeleteMapping("/{cartId}/product/{productId}")
//    public CartDTO removeProductFromCart(@PathVariable long cartId, @PathVariable long productId) {
//        //TODO removeProductFromCart
//    }
//
//    @PutMapping("/{userId}/product/{productId}")
//    public CartDTO updateProductInCart(@PathVariable long userId, @PathVariable long productId, @Param("quantity") int quantity) {
//        //TODO updateProductInCart
//    }
}
