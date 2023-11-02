package com.example.petproject.repository;

import com.example.petproject.entity.Cart;

import com.example.petproject.entity.Order;
import com.example.petproject.entity.enums.CartStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Override
    @NonNull <S extends Cart> S save(@NonNull S entity);

    List<Cart> findAllByUserIdAndStatus(long id, CartStatus status);

    Cart findByUserIdAndStatusAndOrder(long id, CartStatus status, Order order);

    @Query("""
            SELECT c FROM Cart c WHERE c.createDate < :createDate AND c.status = 'NEW'
            """)
    List<Cart> findAllByCreateDateAndStatus(@Param("createDate") LocalDateTime createDate);

    Cart findByUserIdAndStatus(long id, CartStatus cartStatus);


    Cart findCartByIdAndStatus(long id, CartStatus status);

    List<Cart> findAllByIdAndStatus(long id, CartStatus status);

    int deleteByUserIdAndStatus(long id, CartStatus status);

    void deleteById(long cartId);


    @Modifying
    @Query("""
            UPDATE Cart c SET c.sum = c.sum + :newSum
            WHERE c.status = 'NEW' AND c.id = :cartId
            """)
    int updateCartSumWhenStatusNewById(
            @Param("newSum") double newSum, @Param("cartId") long cartId);

    @Modifying
    @Query("""
            UPDATE Cart c SET c.sum = :newSum
            WHERE c.status = 'COMPLETED' AND c.order.id = :orderId
            """)
    int updateCartSumWhenStatusCompletedByOrderId(
            @Param("newSum") double newSum, @Param("orderId") long orderId);

    @Modifying
    @Query("""
            UPDATE Cart c SET c.status = 'CANCELLED' WHERE c.createDate < :createDate AND c.status = 'NEW'
            AND c.order IS NULL
            """)
    int updateCartStatusToCancelledByCreateDate(@Param("createDate") LocalDateTime createDate);

    @Modifying
    @Query("""
            UPDATE Cart c SET c.status = 'COMPLETED'
            WHERE c.order.id = :orderId AND c.status = 'NEW'
            """)
    int updateCartStatusToCompletedByOrderId(@Param("orderId") long orderId);

    @Modifying
    @Query("""
            UPDATE Cart c SET c.order.id = :orderId
            WHERE c.id = :cartId AND c.status = 'NEW'
            """)
    int updateCartOrderByCartId(@Param("cartId") long cartId, @Param("orderId") long orderId);

}
