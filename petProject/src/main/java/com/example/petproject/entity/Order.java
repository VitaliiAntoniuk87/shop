package com.example.petproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
//@ToString(exclude = {"user", "products"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "delivery_type")
    private String deliveryType;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    private String comment;

    @Column(name = "order_recipient_name")
    private String orderRecipientName;

    @Column(name = "order_recipient_phone")
    private String orderRecipientPhone;

    private double sum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cart cart;

}