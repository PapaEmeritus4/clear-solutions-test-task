package com.example.testassignment.service;

import com.example.testassignment.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    UserEntity updateUser(UserEntity user);

    List<UserEntity> getAllUsersByBirthDateRange(LocalDate from, LocalDate to, Pageable pageable);

    void deleteById(Integer id);
}
