package com.example.petproject.service;

import com.example.petproject.dto.CategoryDTO;
import com.example.petproject.entity.Category;
import com.example.petproject.mapper.BaseDtoMapper;
import com.example.petproject.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
@Builder
public class CategoryService {

    private CategoryRepository categoryRepository;
    private BaseDtoMapper baseDtoMapper;

    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(cat -> baseDtoMapper.toDTO(cat, CategoryDTO.class)).toList();
    }
}
