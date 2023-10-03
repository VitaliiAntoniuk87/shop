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
        cartService.cartAutoCancellation(AppConstants.CART_AUTO_CANCELLATION_TIMEOUT_MINUTES);
    }
}
