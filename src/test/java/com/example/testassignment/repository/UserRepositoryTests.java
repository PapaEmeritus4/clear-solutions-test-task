package com.example.testassignment.repository;

import com.example.testassignment.entity.UserEntity;
import com.example.testassignment.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save user functionality")
    public void givenUserObject_whenSaveUser_thenUserIsCreated() {
        //given
        UserEntity userToSave = DataUtils.getJohnDoeTransient();
        //when
        UserEntity savedUser = userRepository.save(userToSave);
        //then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test update user ONE field functionality")
    public void givenUserToUpdate_whenSave_thenEmailIsChanged() {
        //given
        String updatedEmail = "updated@email.com";
        UserEntity userToCreate = DataUtils.getJohnDoeTransient();
        userRepository.save(userToCreate);
        //when
        UserEntity userToUpdate = userRepository.findById(userToCreate.getId())
                .orElse(null);
        userToUpdate.setEmail(updatedEmail);

        UserEntity updatedUser = userRepository.save(userToUpdate);
        //then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo(updatedEmail);
    }

    @Test
    @DisplayName("Test update user SOME fields functionality")
    public void givenUserToUpdate_whenSave_thenEmailAndAddressIsChanged() {
        //given
        String updatedEmail = "updated@email.com";
        String updatedAddress = null;
        UserEntity userToCreate = DataUtils.getJohnDoeTransient();
        userRepository.save(userToCreate);
        //when
        UserEntity userToUpdate = userRepository.findById(userToCreate.getId())
                .orElse(null);
        userToUpdate.setEmail(updatedEmail);
        userToUpdate.setAddress(updatedAddress);

        UserEntity updatedUser = userRepository.save(userToUpdate);
        //then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getAddress()).isNull();
        assertThat(updatedUser.getEmail()).isEqualTo(updatedEmail);
    }

    @Test
    @DisplayName("Test update user ALL fields functionality")
    public void givenUserToUpdate_whenSave_thenAllFieldsIsChanged() {
        //given
        String updatedEmail = "updated@email.com";
        String updatedFirstName = "updatedFirstName";
        String updatedLastName = "updatedLastName";
        LocalDate updatedBirthday = LocalDate.of(1978, 3, 10);
        String updatedAddress = null;
        String updatedPhoneNumber = null;
        UserEntity userToCreate = DataUtils.getJohnDoeTransient();
        userRepository.save(userToCreate);
        //when
        UserEntity userToUpdate = userRepository.findById(userToCreate.getId())
                .orElse(null);
        userToUpdate.setEmail(updatedEmail);
        userToUpdate.setFirstName(updatedFirstName);
        userToUpdate.setLastName(updatedLastName);
        userToUpdate.setBirthDate(updatedBirthday);
        userToUpdate.setAddress(updatedAddress);
        userToUpdate.setPhoneNumber(updatedPhoneNumber);

        UserEntity updatedUser = userRepository.save(userToUpdate);
        //then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo(updatedEmail);
        assertThat(updatedUser.getFirstName()).isEqualTo(updatedFirstName);
        assertThat(updatedUser.getLastName()).isEqualTo(updatedLastName);
        assertThat(updatedUser.getBirthDate()).isEqualTo(updatedBirthday);
        assertThat(updatedUser.getAddress()).isNull();
        assertThat(updatedUser.getPhoneNumber()).isNull();
    }

    //TODO test findByBirthDateBetween()
    @Test
    @DisplayName("Test find all users from first date to second date")
    public void givenThreeUsers_whenFindByBirthDateBetween_thenAllNeededUsersAreReturned() {
        //given
        UserEntity user1 = DataUtils.getJohnDoeTransient();//1990-5-15
        UserEntity user2 = DataUtils.getMikeSmithTransient();//1985-8-20
        UserEntity user3 = DataUtils.getFrankJonesTransient();//1978-3-10

        userRepository.saveAll(List.of(user1, user2, user3));
        //when
        LocalDate from = LocalDate.of(1985, 3, 10);
        LocalDate to = LocalDate.of(1991, 5, 15);

        List<UserEntity> users = userRepository.findByBirthDateBetween(from, to, Pageable.unpaged());
        //then
        assertThat(CollectionUtils.isEmpty(users.stream().toList())).isFalse();
        assertThat(users.stream().count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test delete by id functionality")
    public void givenUserIsSaved_whenDeleteById_thenUserIsRemovedFromDB() {
        //given
        UserEntity user = DataUtils.getJohnDoePersisted();
        userRepository.save(user);
        //when
        userRepository.deleteById(user.getId());
        //then
        UserEntity obtainedUser = userRepository.findById(user.getId())
                .orElse(null);
        assertThat(obtainedUser).isNull();
    }
}
