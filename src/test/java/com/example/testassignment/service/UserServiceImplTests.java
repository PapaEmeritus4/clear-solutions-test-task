package com.example.testassignment.service;

import com.example.testassignment.entity.UserEntity;
import com.example.testassignment.exception.BirthDateRangeException;
import com.example.testassignment.exception.UserNotAdultException;
import com.example.testassignment.exception.UserNotFoundException;
import com.example.testassignment.repository.UserRepository;
import com.example.testassignment.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl serviceUnderTest;

    @Test
    @DisplayName("Test save user functionality")
    public void givenUserToSave_whenSaveUser_thenRepositoryIsCalled() {
        //given
        UserEntity userToSave = DataUtils.getJohnDoeTransient();
        BDDMockito.given(userRepository.save(any(UserEntity.class)))
                .willReturn(DataUtils.getJohnDoeTransient());
        //when
        UserEntity savedUser = serviceUnderTest.saveUser(userToSave);
        //then
        assertThat(savedUser).isNotNull();
    }

    @Test
    @DisplayName("Test save not adult user functionality")
    public void givenUserToSaveWithBirthDateUnder18_whenSaveUser_thenExceptionIsThrown() {
        //given
        UserEntity userToSave = DataUtils.getJohnDoeTransientNotAdult();
        BDDMockito.given(userRepository.save(any(UserEntity.class)))
                .willThrow(UserNotAdultException.class);
        //when
        assertThrows(
               UserNotAdultException.class, () -> serviceUnderTest.saveUser(userToSave)
       );
        //then
    }

    @Test
    @DisplayName("Test update user functionality")
    public void givenUserToUpdate_whenUpdateUser_thenRepositoryIsCalled() {
        //given
        UserEntity userToUpdate = DataUtils.getJohnDoePersisted();
        BDDMockito.given(userRepository.existsById(anyInt()))
                .willReturn(true);
        BDDMockito.given(userRepository.save(any(UserEntity.class)))
                .willReturn(userToUpdate);
        //when
        UserEntity updatedUser = serviceUnderTest.updateUser(userToUpdate);
        //then
        assertThat(updatedUser).isNotNull();
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test update user with incorrect id functionality")
    public void givenUserToUpdateWithIncorrectId_whenUpdateUser_thenExceptionIsThrown() {
        //given
        UserEntity userToUpdate = DataUtils.getJohnDoePersisted();
        BDDMockito.given(userRepository.existsById(anyInt()))
                .willReturn(false);
        //when
        assertThrows(
                UserNotFoundException.class, () -> serviceUnderTest.updateUser(userToUpdate)
        );
        //then
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test get users from first date to second date")
    public void givenThreeUsers_whenGetAllUsersByBirthDateRange_thenOnlyNeededWillBeReturned() {
        //given
        LocalDate from = LocalDate.of(1984, 3, 10);
        LocalDate to = LocalDate.of(1991, 5, 15);
        UserEntity user1 = DataUtils.getJohnDoePersisted();//1990-5-15
        UserEntity user2 = DataUtils.getMikeSmithPersisted();//1985-8-20

        List<UserEntity> users = List.of(user1, user2);
        BDDMockito.given(userRepository.findByBirthDateBetween(from, to, Pageable.unpaged()))
                .willReturn(users);
        //when
        List<UserEntity> obtainedUsers = serviceUnderTest.getAllUsersByBirthDateRange(from, to, Pageable.unpaged());
        //then
        assertThat(obtainedUsers).isNotNull();
        assertThat(obtainedUsers.size()).isEqualTo(2);
        assertThat(obtainedUsers).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    @DisplayName("Test get users with incorrect from date")
    public void givenThreeUsersWithIncorrectFromDate_whenGetAllUsersByBirthDateRange_thenExceptionIsThrown() {
        //given
        LocalDate from = LocalDate.of(1991, 3, 10);
        LocalDate to = LocalDate.of(1984, 5, 15);
        //when
        assertThrows(BirthDateRangeException.class, () -> serviceUnderTest.getAllUsersByBirthDateRange(from, to, Pageable.unpaged()));
        //then
    }

    @Test
    @DisplayName("Test delete by id functionality")
    public void givenCorrectId_whenDeleteById_thenDeleteRepoMethodIsCalled() {
        //given
        BDDMockito.given(userRepository.findById(anyInt()))
                .willReturn(Optional.of(DataUtils.getJohnDoePersisted()));
        //when
        serviceUnderTest.deleteById(1);
        //then
        verify(userRepository, times(1)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Test hard delete by incorrect id functionality")
    public void givenIncorrectId_whenDeleteById_thenExceptionIsThrown() {
        //given
        BDDMockito.given(userRepository.findById(anyInt()))
                .willReturn(Optional.empty());
        //when
        assertThrows(UserNotFoundException.class, () -> serviceUnderTest.deleteById(1));
        //then
        verify(userRepository, never()).deleteById(anyInt());
    }
}
