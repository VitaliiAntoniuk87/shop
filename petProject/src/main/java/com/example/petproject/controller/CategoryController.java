package com.example.petproject.controller;

import com.example.petproject.dto.CategoryDTO;
import com.example.petproject.entity.Category;
import com.example.petproject.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }
}
