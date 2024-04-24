package com.example.testassignment.repository;

import com.example.testassignment.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Page<UserEntity> findByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);
}
