package com.example.petproject.entity;

import com.example.petproject.entity.enums.DeliveryType;
import com.example.petproject.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
//@ToString(exclude = {"user", "products"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders", schema = "shop")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "delivery_type")
    @Enumerated(value = EnumType.STRING)
    private DeliveryType deliveryType;

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

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cart cart;

}
