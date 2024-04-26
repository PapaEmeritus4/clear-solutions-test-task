package com.example.testassignment.rest;

import com.example.testassignment.dto.UserDto;
import com.example.testassignment.entity.UserEntity;
import com.example.testassignment.exception.BirthDateRangeException;
import com.example.testassignment.exception.UserNotAdultException;
import com.example.testassignment.exception.UserNotFoundException;
import com.example.testassignment.service.UserService;
import com.example.testassignment.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
public class UserRestControllerV1Tests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Test create user functionality")
    public void givenUserDto_whenCreateUser_thenSuccessResponse() throws Exception {
        //given
        UserDto userDto = DataUtils.getJohnDoeDtoTransient();
        UserEntity entity = DataUtils.getJohnDoePersisted();
        BDDMockito.given(userService.saveUser(any(UserEntity.class)))
                .willReturn(entity);
        //when
        ResultActions result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(entity.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(entity.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(entity.getEmail())));
    }

    @Test
    @DisplayName("Test create not adult user functionality")
    public void givenUserDtoNotAdult_whenCreateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userDto = DataUtils.getJohnDoeDtoTransient();
        BDDMockito.given(userService.saveUser(any(UserEntity.class)))
                .willThrow(new UserNotAdultException("User must be at least 18 years old."));
        //when
        ResultActions result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.message", CoreMatchers.is("User must be at least 18 years old.")));
    }

    @Test
    @DisplayName("Test update user functionality")
    public void givenUserDto_whenUpdateUser_thenSuccessResponse() throws Exception {
        //given
        UserDto developerDto = DataUtils.getJohnDoeDtoPersisted();
        UserEntity entity = DataUtils.getFrankJonesPersisted();
        BDDMockito.given(userService.updateUser(any(UserEntity.class)))
                .willReturn(entity);
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(developerDto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(entity.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(entity.getLastName())));
    }

    @Test
    @DisplayName("Test update user with incorrect id functionality")
    public void givenUserDtoWithIncorrectId_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userDto = DataUtils.getJohnDoeDtoTransient();
        BDDMockito.given(userService.updateUser(any(UserEntity.class)))
                .willThrow(new UserNotFoundException("User not found."));
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.message", CoreMatchers.is("User not found.")));
    }

    @Test
    @DisplayName("Test delete functionality")
    public void givenId_whenDelete_thenSuccessResponse() throws Exception {
        //given
        BDDMockito.doNothing().when(userService).deleteById(anyInt());
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(userService, times(1)).deleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete by incorrect id functionality")
    public void givenIncorrectId_whenDelete_thenErrorResponse() throws Exception {
        //given
        BDDMockito.doThrow(new UserNotFoundException("User not found."))
                .when(userService).deleteById(anyInt());
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(userService, times(1)).deleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Test get users by birth date range functionality")
    public void givenUsers_whenGetUsersByBirthDateRange_thenSuccessResponse() throws Exception {
        //given
        LocalDate from = LocalDate.of(1984, 1, 1);
        LocalDate to = LocalDate.of(1992, 12, 31);
        UserEntity user1 = DataUtils.getJohnDoePersisted();
        UserEntity user2 = DataUtils.getMikeSmithPersisted();

        List<UserEntity> users = List.of(user1, user2);
        BDDMockito.given(userService.getAllUsersByBirthDateRange(from, to, Pageable.unpaged()))
                .willReturn(users);
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/users?from=1984-01-01&to=1992-12-31")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users))
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test get users by birth date range with incorrect date from functionality")
    public void givenUsers_whenGetUsersByBirthDateRange_thenErrorResponse() throws Exception {
        //given
        LocalDate from = LocalDate.of(1992, 1, 1);
        LocalDate to = LocalDate.of(1984, 1, 1);

        BDDMockito.given(userService.getAllUsersByBirthDateRange(from, to, Pageable.unpaged()))
                .willThrow(new BirthDateRangeException("'From' date must be less than 'To' date."));
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/users")
                .param("from", "1992-01-01")
                .param("to", "1984-01-01")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("'From' date must be less than 'To' date.")));
    }
}
