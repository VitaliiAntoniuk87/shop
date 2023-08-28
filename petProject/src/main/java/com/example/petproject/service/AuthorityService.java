package com.example.petproject.service;

import com.example.petproject.entity.Authority;
import com.example.petproject.repository.AuthorityRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class AuthorityService {

    private AuthorityRepository authorityRepository;

    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    public List<Authority> findAllByUserId(long id) {
        return authorityRepository.findAllByUsersId(id);
    }
}
