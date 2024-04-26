package com.example.testassignment.util;

import com.example.testassignment.entity.UserEntity;

import java.time.LocalDate;

public class DataUtils {
    public static UserEntity getJohnDoeTransient() {
        return UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
    }

    public static UserEntity getMikeSmithTransient() {
        return UserEntity.builder()
                .firstName("Mike")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address("456 Oak St")
                .phoneNumber("987-654-3210")
                .build();
    }

    public static UserEntity getFrankJonesTransient() {
        return UserEntity.builder()
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@mail.com")
                .birthDate(LocalDate.of(1978, 3, 10))
                .address("789 Elm St")
                .phoneNumber("555-123-4567")
                .build();
    }

    public static UserEntity getJohnDoePersisted() {
        return UserEntity.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
    }

    public static UserEntity getMikeSmithPersisted() {
        return UserEntity.builder()
                .id(2)
                .firstName("Mike")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address("456 Oak St")
                .phoneNumber("987-654-3210")
                .build();
    }

    public static UserEntity getFrankJonesPersisted() {
        return UserEntity.builder()
                .id(3)
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@mail.com")
                .birthDate(LocalDate.of(1978, 3, 10))
                .address("789 Elm St")
                .phoneNumber("555-123-4567")
                .build();
    }
}
