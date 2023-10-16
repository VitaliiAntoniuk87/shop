package com.example.petproject.dto;

import com.example.petproject.entity.enums.DeliveryType;
import com.example.petproject.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private long id;
    private DeliveryType deliveryType;
    private String deliveryAddress;
    private LocalDateTime deliveryDate;
    private String comment;
    private String orderRecipientName;
    private String orderRecipientPhone;
    private OrderStatus status;
    private LocalDateTime createDate;
    private LocalDateTime purchaseDate;
    private CartDTO cart;
}
