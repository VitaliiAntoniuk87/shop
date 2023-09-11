package com.example.petproject.repository;

import com.example.petproject.entity.ProductCart;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, Long> {

    @Override
    @NonNull <S extends ProductCart> List<S> saveAll(@NonNull Iterable<S> entities);

    @Override
    @NonNull <S extends ProductCart> S save(@NonNull S entity);

    List<ProductCart> findAllByCartId(long cartId);

    int deleteAllByCartId(long id);

    int deleteAllByCartIdAndProductId(long cartId, long productId);

    @Modifying
    @Query("""
            UPDATE ProductCart pc SET pc.quantity = :newQuantity, pc.price = :newPrice, pc.total = :newTotal
            WHERE pc.product.id = :productId AND pc.cart.id = :cartId
            """)
    int updateProductCartQuantityPriceTotal(
            @Param("newQuantity") int newQuantity, @Param("newPrice") double newPrice,
            @Param("newTotal") double newTotal, @Param("productId") long productId, @Param("cartId") long cartId);

    @Modifying
    @Query("""
            UPDATE ProductCart pc SET pc.quantity = :newQuantity, pc.total = :newTotal
            WHERE pc.product.id = :productId AND pc.cart.id = :cartId
            """)
    int updateProductCartQuantityTotal(
            @Param("newQuantity") int newQuantity, @Param("newTotal") double newTotal,
            @Param("productId") long productId, @Param("cartId") long cartId);

    @Modifying
    @Query("""
            UPDATE ProductCart pc SET pc.price = :newPrice, pc.total = :newTotal
            WHERE pc.product.id = :productId AND pc.cart.id = :cartId
            """)
    int updateProductCartPriceTotal(
            @Param("newPrice") double newPrice, @Param("newTotal") double newTotal,
            @Param("productId") long productId, @Param("cartId") long cartId);

    @Modifying
    @Query("""
            UPDATE ProductCart pc SET pc.quantity = pc.quantity + :newQuantityDiff, pc.total = pc.total + :newTotalDiff
            WHERE pc.product.id = :productId AND pc.cart.id = :cartId
            """)
    int updateProductCartQuantityTotalByDifference(
            @Param("newQuantityDiff") int newQuantityDiff, @Param("newTotalDiff") double newTotalDiff,
            @Param("productId") long productId, @Param("cartId") long cartId);


}
