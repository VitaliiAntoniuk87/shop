package com.example.petproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products", schema = "shop")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product")
    private String name;

    private int quantity;

    @Column(name = "current_price", precision = 10, scale = 2)
    private double currentPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCart> carts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
