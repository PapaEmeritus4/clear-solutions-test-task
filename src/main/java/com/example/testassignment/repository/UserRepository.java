package com.example.testassignment.repository;

import com.example.testassignment.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);
}
