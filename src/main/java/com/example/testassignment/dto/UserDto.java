package com.example.testassignment.dto;

import com.example.testassignment.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static UserDto fromEntity(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .birthDate(userEntity.getBirthDate())
                .address(userEntity.getAddress())
                .phoneNumber(userEntity.getPhoneNumber())
                .build();
    }
}
