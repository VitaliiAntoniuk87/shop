package com.example.petproject.repository;

import com.example.petproject.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Override
    List<Authority> findAll();

    List<Authority> findAllByUsersId(long id);
}
