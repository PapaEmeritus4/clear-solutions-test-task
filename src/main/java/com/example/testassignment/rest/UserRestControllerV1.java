package com.example.testassignment.rest;

import com.example.testassignment.dto.ErrorDto;
import com.example.testassignment.dto.UserDto;
import com.example.testassignment.entity.UserEntity;
import com.example.testassignment.exception.BirthDateRangeException;
import com.example.testassignment.exception.UserNotAdultException;
import com.example.testassignment.exception.UserNotFoundException;
import com.example.testassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserRestControllerV1 {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto dto) {
        try {
            UserEntity entity = dto.toEntity();
            UserEntity createdEntity = userService.saveUser(entity);
            UserDto result = UserDto.fromEntity(createdEntity);
            return ResponseEntity.ok(result);
        } catch (UserNotAdultException e) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
                    );
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto dto) {
        try {
            UserEntity entity = dto.toEntity();
            UserEntity updatedEntity = userService.updateUser(entity);
            UserDto result = UserDto.fromEntity(updatedEntity);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
                    );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
                    );
        }
    }

    @GetMapping
    public ResponseEntity<?> getUsersByBirthDateRange(
            @RequestParam(value = "from") LocalDate from,
            @RequestParam(value = "to") LocalDate to,
            Pageable pageable) {
        try {
            List<UserEntity> users = userService.getAllUsersByBirthDateRange(from, to, pageable);
            return ResponseEntity.ok(users);
        } catch (BirthDateRangeException e) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
                    );
        }

    }
}
