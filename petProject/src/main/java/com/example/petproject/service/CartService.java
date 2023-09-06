package com.example.petproject.service;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.CartStatus;
import com.example.petproject.entity.Product;
import com.example.petproject.entity.ProductCart;
import com.example.petproject.mapper.CartDtoMapper;
import com.example.petproject.mapper.ProductCartDtoMapper;
import com.example.petproject.repository.CartRepository;
import com.example.petproject.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@Builder
public class CartService {

    private final CartDtoMapper cartDtoMapper;
    private final ProductCartDtoMapper productCartDtoMapper;
    private final ProductService productService;
    private final ProductCartService productCartService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartDTO getCartByUserId(long id) {
        Cart cart = cartRepository.findByUserIdAndStatus(id, CartStatus.NEW);
        return cartDtoMapper.toCartDTO(cart);
    }

    @Transactional
    public CartDTO saveCart(CartDTO cartDTO) {
        if (productService.isProductsPriceAndQuantityCorrect(cartDTO)) {
            Cart cart = cartDtoMapper.toCartEntity(cartDTO);
            cart.setCreateDate(LocalDateTime.now());
            Cart savedCart = cartRepository.save(cart);

            List<ProductCart> productCart = productCartDtoMapper.toProductCartEntityList(cartDTO.getProducts());
            productCart.forEach(pc -> pc.setCart(savedCart));
            productCartService.saveAll(productCart);

            cartDTO.setId(savedCart.getId());
            cartDTO.setCreateDate(LocalDateTime.now());
            productService.reduceProductQuantity(cartDTO.getProducts());
        }
        return cartDTO;
    }

    @Transactional
    public int deleteCart(CartDTO cartDTO) {
        productService.increaseQuantity(cartDTO.getProducts());
        return cartRepository.deleteById(cartDTO.getId());
    }

    @Transactional
    public CartDTO addProductToCart(long cartId, long productId, int quantity) {
        Product product = productRepository.findProductById(productId);
        Cart cart = cartRepository.findCartById(cartId);
        ProductCart productCart = ProductCart.builder()
                .product(product)
                .cart(cart)
                .price(product.getCurrentPrice())
                .quantity(quantity)
                .total(MathServices.roundToHundredths(quantity * product.getCurrentPrice()))
                .build();
        productCartService.save(productCart);
        cartRepository.updateCartSumWhenStatusNewById(productCart.getTotal(), cartId);
        productService.reduceProductQuantity(productId, quantity);

        Cart updatedCart = cartRepository.findCartById(cartId);
        System.out.println(updatedCart.getSum());
        return cartDtoMapper.toCartDTO(updatedCart);

    }

//    public CartDTO removeProductFromCart(long cartId, long productId, int quantity) {
//
//    }


}
