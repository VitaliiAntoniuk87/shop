package com.example.petproject.service;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.CartStatus;
import com.example.petproject.entity.Product;
import com.example.petproject.entity.ProductCart;
import com.example.petproject.exception.ObjectAlreadyExistException;
import com.example.petproject.mapper.CartDtoMapper;
import com.example.petproject.mapper.ProductCartDtoMapper;
import com.example.petproject.repository.CartRepository;
import com.example.petproject.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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
        if (cart != null) {
            return cartDtoMapper.toCartDTO(cart);
        } else {
            throw new NoSuchElementException("Active cart not found by this user id!");
        }
    }

    public CartDTO getCartByIdAndStatus(long id) {
        Cart cart = cartRepository.findCartByIdAndStatus(id, CartStatus.NEW);
        if (cart != null) {
            return cartDtoMapper.toCartDTO(cart);
        } else {
            throw new NoSuchElementException("Active cart not found by this id!");
        }
    }

    @Transactional
    public CartDTO saveCart(CartDTO cartDTO) {
        Cart cartFromDB = cartRepository.findByUserIdAndStatus(cartDTO.getUserId(), CartStatus.NEW);
        if (cartFromDB == null) {
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
        } else {
            try {
                throw new ObjectAlreadyExistException("Cart is Already Exist");
            } catch (ObjectAlreadyExistException e) {
                 //TODO handler
            }
        }
    }

    @Transactional
    public void deleteCart(long cartId) {
        Cart cart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
        productService.increaseProductQuantityWithEntity(cart.getProducts());
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public CartDTO addProductToCart(long cartId, long productId, int quantity) {
        Product product = productRepository.findProductById(productId);
        if (product.getQuantity() >= quantity) {
            Cart cart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
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
            double newSum = cart.getSum() + productCart.getTotal();
            Cart updatedCart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
            updatedCart.setSum(newSum);
            return cartDtoMapper.toCartDTO(updatedCart);
        }
        return null;
    }

    @Transactional
    public void removeProductFromCart(long cartId, long productId) {
        ProductCart productCart = getProductCartByCartIdProductId(cartId, productId);

        productCartService.deleteAllByCartIdAndProductId(cartId, productId);
        cartRepository.updateCartSumWhenStatusNewById(-1 * productCart.getTotal(), cartId);
        productService.increaseProductQuantityWithEntity(productCart);
    }

    @Transactional
    public void updateProductInCart(long cartId, long productId, int quantity) {
        ProductCart productCart = getProductCartByCartIdProductId(cartId, productId);
        int quantityDifference = quantity - productCart.getQuantity();
        if (quantityDifference > 0) {
            Product product = productRepository.findProductById(productId);
            if (product.getQuantity() >= quantityDifference) {
                updateCartAndProductAndProductCartAtDB(cartId, quantity, productCart);
                productService.reduceProductQuantity(productId, quantityDifference);

            }
        } else {
            updateCartAndProductAndProductCartAtDB(cartId, quantity, productCart);
            productService.increaseQuantity(productId, quantityDifference);
        }
    }

    private ProductCart getProductCartByCartIdProductId(long cartId, long productId) {
        Cart cart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
        return cart.getProducts().stream()
                .filter(pc -> pc.getProduct().getId() == productId).findFirst().orElseThrow(() -> new ProductCartNotFoundException("ProductCart not found for Cart ID: " + cartId + " and Product ID: " + productId)); //TODO

    }

    @Transactional
    public void updateCartAndProductAndProductCartAtDB(long cartId, int quantity, ProductCart productCart) {
        double oldTotal = productCart.getTotal();
        productCart.setQuantity(quantity);
        productCart.setTotal(MathServices.roundToHundredths(productCart.getQuantity() * productCart.getPrice()));
        productCartService.updateProductCartQuantityTotal(productCart);
        cartRepository.updateCartSumWhenStatusNewById(productCart.getTotal() - oldTotal, cartId);
    }

}
