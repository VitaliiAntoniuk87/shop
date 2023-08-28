package com.example.petproject.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {

    private long id;
    private String name;

}
