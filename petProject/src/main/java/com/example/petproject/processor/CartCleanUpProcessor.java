package com.example.petproject.processor;

import com.example.petproject.constants.AppConstants;
import com.example.petproject.service.CartService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Builder
public class CartCleanUpProcessor extends BatchProcessor {

    private final CartService cartService;

    @Override
    public void run() {
        cartService.cartAutoCancellation(AppConstants.NEW_CART_TIMEOUT_TO_AUTO_CANCELLATION_AFTER_CREATION_MINUTES);
    }
}
