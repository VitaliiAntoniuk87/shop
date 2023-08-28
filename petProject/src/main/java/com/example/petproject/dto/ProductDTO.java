package com.example.petproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private long id;
    private String name;
    private int quantity;
    private double currentPrice;

}
