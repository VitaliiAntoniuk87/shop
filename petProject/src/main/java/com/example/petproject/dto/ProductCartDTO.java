package com.example.petproject.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartDTO {

    private long id;
    private ProductDTO product;
    private int quantity;
    private double price;
    private double total;
}
