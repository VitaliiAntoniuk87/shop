package com.example.petproject.dto;

import com.example.petproject.entity.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {

    private long id;
    private List<ProductCartDTO> products;
    private long userId;
    private Double sum;
    private LocalDateTime createDate;
}
