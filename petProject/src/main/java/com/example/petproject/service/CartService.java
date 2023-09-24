package com.example.petproject.service;

import com.example.petproject.dto.CartDTO;
import com.example.petproject.dto.ProductCartDTO;
import com.example.petproject.entity.Cart;
import com.example.petproject.entity.CartStatus;
import com.example.petproject.entity.Product;
import com.example.petproject.entity.ProductCart;
import com.example.petproject.exception.IncorrectPriceQuantityException;
import com.example.petproject.exception.ObjectFieldWrongValueException;
import com.example.petproject.exception.ObjectNotFoundException;
import com.example.petproject.mapper.CartDtoMapper;
import com.example.petproject.mapper.ProductCartDtoMapper;
import com.example.petproject.repository.CartRepository;
import com.example.petproject.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
@Builder
@Log4j2
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
            throw new ObjectNotFoundException("Active cart not found for this User!");
        }
    }

    public CartDTO getCartById(long id) {
        Cart cart = cartRepository.findCartByIdAndStatus(id, CartStatus.NEW);
        if (cart != null) {
            return cartDtoMapper.toCartDTO(cart);
        } else {
            throw new ObjectNotFoundException("Active cart not found");
        }
    }

    @Transactional
    public CartDTO saveCart(CartDTO cartDTO) {
        if (cartDTO.getUserId() > 0 && !cartDTO.getProducts().isEmpty()) {
            Cart cartFromDB = cartRepository.findByUserIdAndStatus(cartDTO.getUserId(), CartStatus.NEW);
            List<ProductCartDTO> productCartDTOS = cartDTO.getProducts();
            if (productService.isProductsPriceAndQuantityCorrect(productCartDTOS)) {
                productCartDTOS.forEach(p -> p.setTotal(p.getPrice() * p.getQuantity()));
                if (cartFromDB == null) {
                    Cart cart = cartDtoMapper.toCartEntity(cartDTO);
                    cart.setSum(productCartDTOS.stream().mapToDouble(ProductCartDTO::getTotal).sum());
                    cart.setCreateDate(LocalDateTime.now());
                    cartRepository.save(cart);

                    List<ProductCart> productCart = productCartDtoMapper.toProductCartEntityList(productCartDTOS);
                    productCart.forEach(pc -> pc.setCart(cart));
                    productCartService.saveAll(productCart);

                    productService.decrementProductQuantity(productCartDTOS);
                    cart.setProducts(productCart);
                    return cartDtoMapper.toCartDTO(cart);
                } else {
                    log.warn("Active cart for userId " + cartDTO.getUserId() + " is already exist." +
                            "Invoke method  updateProductsInCartFilter ");
                    updateProductsInCartFilter(cartFromDB, productCartDTOS);
                    return getCartById(cartFromDB.getId());
                }
            } else {
                throw new IncorrectPriceQuantityException("Product price or quantity is incorrect");
            }
        } else {
            throw new ObjectFieldWrongValueException("UserId or productList are empty");
        }
//        return null;
    }

    @Transactional
    public void deleteCart(long cartId) {
        Cart cart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
        productService.incrementProductQuantityWithEntity(cart.getProducts());
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
            productService.decrementProductQuantity(productId, quantity);
            double newSum = cart.getSum() + productCart.getTotal();
            Cart updatedCart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
            updatedCart.setSum(newSum);
            return cartDtoMapper.toCartDTO(updatedCart);
        }
        return null;
    }

    @Transactional
    public void removeProductFromCart(long cartId, long productId) {
        Cart cart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
        ProductCart productCart = cart.getProducts().stream()
                .filter(pc -> pc.getProduct().getId() == productId).findFirst().orElse(null);
        if (productCart != null) {
            if (cart.getProducts().size() > 1) {
                productCartService.deleteAllByCartIdAndProductId(cartId, productId);
                cartRepository.updateCartSumWhenStatusNewById(-1 * productCart.getTotal(), cartId);
                productService.incrementProductQuantityWithEntity(productCart);
            } else {
                deleteCart(cartId);
            }
        } else {
            throw new ObjectNotFoundException("Product with id " + productId + " not found at cart " + cartId);
        }


    }

    @Transactional
    public void updateProductInCart(long cartId, long productId, int quantity) {
        if (quantity > 0) {
            ProductCart productCart = getProductCartByCartIdProductId(cartId, productId);
            int quantityDifference = quantity - productCart.getQuantity();
            if (quantityDifference > 0) {
                Product product = productRepository.findProductById(productId);
                if (product.getQuantity() >= quantityDifference) {
                    updateCartAndProductAndProductCartAtDB(cartId, quantity, productCart);
                    productService.decrementProductQuantity(productId, quantityDifference);

                }
            } else {
                updateCartAndProductAndProductCartAtDB(cartId, quantity, productCart);
                productService.incrementQuantity(productId, quantityDifference);
            }
        } else if (quantity == 0) {
            removeProductFromCart(cartId, productId);
        } else {
            throw new ObjectFieldWrongValueException("parameter quantity has wrong value");
        }
    }

    private ProductCart getProductCartByCartIdProductId(long cartId, long productId) {
        Cart cart = cartRepository.findCartByIdAndStatus(cartId, CartStatus.NEW);
        return cart.getProducts().stream()
                .filter(pc -> pc.getProduct().getId() == productId).findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("ProductCart not found for Product ID: " + productId));

    }

    @Transactional
    public void updateCartAndProductAndProductCartAtDB(long cartId, int quantity, ProductCart productCart) {
        double oldTotal = productCart.getTotal();
        productCart.setQuantity(quantity);
        productCart.setTotal(MathServices.roundToHundredths(productCart.getQuantity() * productCart.getPrice()));
        productCartService.updateProductCartQuantityTotal(productCart);
        cartRepository.updateCartSumWhenStatusNewById(productCart.getTotal() - oldTotal, cartId);
    }

    public void updateProductsInCartFilter(Cart cart, List<ProductCartDTO> productsToAdd) {
        List<ProductCartDTO> newProducts = new ArrayList<>();
        List<ProductCartDTO> oldProducts = new ArrayList<>();

        for (ProductCartDTO productToAdd : productsToAdd) {
            boolean found = cart.getProducts().stream()
                    .anyMatch(pc -> pc.getProduct().getId() == productToAdd.getProductId());

            (found ? oldProducts : newProducts).add(productToAdd);
        }

        if (!newProducts.isEmpty()) {
            addProductsToCart(cart, newProducts);
        }
        if (!oldProducts.isEmpty()) {
            updateProductsInCart(cart, oldProducts);
        }

    }

    @Transactional
    public void addProductsToCart(Cart cart, List<ProductCartDTO> productCartDTOS) {
        List<ProductCart> productCarts = productCartDtoMapper.toProductCartEntityList(productCartDTOS);
        productCarts.forEach(pc -> pc.setCart(cart));
        productCartService.saveAll(productCarts);
        productService.decrementProductQuantity(productCartDTOS);

        double newSum = productCartDTOS.stream().mapToDouble(ProductCartDTO::getTotal).sum();
        cartRepository.updateCartSumWhenStatusNewById(newSum, cart.getId());
    }

    @Transactional
    public void updateProductsInCart(Cart cart, List<ProductCartDTO> productCartDTOS) {
        double newSum = productCartDTOS.stream().mapToDouble(ProductCartDTO::getTotal).sum();
        cartRepository.updateCartSumWhenStatusNewById(newSum, cart.getId());
        productService.decrementProductQuantity(productCartDTOS);
        productCartService.updateProductCartQuantityTotalByDifference(cart.getId(), productCartDTOS);
    }

    public void cartCancellation(int timeLimitMinutes) {

    }

    private List<ProductCart> getSumQuantityGroupedByProductFromCartList(List<Cart> carts) {
        return carts.stream().flatMap(c -> c.getProducts().stream())
                .collect(Collectors.groupingBy(
                        ProductCart::getProduct,
                        Collectors.summingInt(ProductCart::getQuantity)
                ))
                .entrySet().stream()
                .map(e -> ProductCart.builder().product(e.getKey()).quantity(e.getValue()).build())
                .toList();
    }

}
