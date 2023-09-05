package com.example.petproject.repository;

import com.example.petproject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByCategoryId(long categoryId);

    @Override
    List<Product> findAll();

    List<Product> findAllByIdIn(List<Long> ids);

    @Override
    List<Product> findAllById(Iterable<Long> ids);

    @Override
    Optional<Product> findById(Long aLong);

    Product findProductById(long id);

    @Modifying
    @Query("""
            UPDATE Product p SET p.quantity = p.quantity + :value WHERE p.id = :id
            """)
    int updateQuantity(@Param("id") long id, @Param("value") int value);

}
