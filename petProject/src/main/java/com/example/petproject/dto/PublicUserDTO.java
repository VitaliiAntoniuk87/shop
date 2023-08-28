package com.example.petproject.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicUserDTO {

    private long id;
    private String name;
    private String email;
    private String phone;

}
