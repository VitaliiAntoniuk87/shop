package com.example.petproject.repository;

import com.example.petproject.entity.Cart;

import com.example.petproject.entity.CartStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Override
    @NonNull <S extends Cart> S save(@NonNull S entity);

    List<Cart> findAllByUserIdAndStatus(long id, CartStatus status);

    Cart findByUserIdAndStatus(long id, CartStatus status);

    Cart findCartByIdAndStatus(long id, CartStatus status);

    List<Cart> findAllByIdAndStatus(long id, CartStatus status);

    int deleteByUserIdAndStatus(long id, CartStatus status);

    int deleteById(long cartId);


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
            """)
    int updateCartStatusToCancelledByCreateDate(@Param("createDate") Timestamp createDate);

    @Modifying
    @Query("""
            UPDATE Cart c SET c.status = 'COMPLETED', c.order.id = :orderId
            WHERE c.id = :cartId AND c.status = 'NEW'
            """)
    int updateCartStatusToCompletedById(@Param("cartId") long cartId, @Param("orderId") long orderId);


}
