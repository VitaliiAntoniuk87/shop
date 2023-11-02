package com.example.petproject.repository;

import com.example.petproject.entity.Order;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByUserId(long id);

    Order findOrderById(long id);

    @Override
    List<Order> findAll();

    List<Order> findAllByStatus(String status);

    @Override
    <S extends Order> S save(S entity);

    @Override
    void deleteById(Long aLong);


}
