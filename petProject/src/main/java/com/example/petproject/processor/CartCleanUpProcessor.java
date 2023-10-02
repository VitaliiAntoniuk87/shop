package com.example.petproject.processor;


import com.example.petproject.interfaces.CartServiceInterface;
import com.example.petproject.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartCleanUpProcessor extends BatchProcessor {

    private final CartService cartService;

    @Override
    public void run() {
        cartService.cartAutoCancellation(2880);
    }
}
