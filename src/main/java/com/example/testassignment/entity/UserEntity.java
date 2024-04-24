package com.example.testassignment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email(message = "Invalid email address")
    @NotNull(message = "Email should be not empty")
    private String email;

    @NotNull(message = "First Name should be not empty")
    private String firstName;

    @NotNull(message = "Last Name should be not empty")
    private String lastName;

    @NotNull(message = "Birth date should be not empty")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Null
    private String address;

    @Null
    private String phoneNumber;
}
