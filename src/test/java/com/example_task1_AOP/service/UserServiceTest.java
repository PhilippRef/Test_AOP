package com.example_task1_AOP.service;

import com.example_task1_AOP.dto.UserDto;
import com.example_task1_AOP.entity.UserDB;
import com.example_task1_AOP.exception.UserNotFoundException;
import com.example_task1_AOP.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserRepository userRepository =
            Mockito.mock(UserRepository.class);

    private final UserService userService =
            new UserService(userRepository);

    private UserDto userDto;
    private UserDB userDB;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDB = new UserDB();
        fillingDB();
    }

    @Test
    @DisplayName("Test get all users")
    void testGetALlUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userDB));

        Collection<UserDto> result = userService.getALlUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + 1 + " не найден"))
                .getId());
        assertEquals("Test User", result.stream()
                .findFirst()
                .orElseThrow()
                .getName());
        assertEquals("test@test.com", result.stream()
                .findFirst()
                .orElseThrow()
                .getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test get user by email")
    void getUserByEmail() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(userDB);

        UserDto result = userService.getUserByEmail("test@test.com");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@test.com", result.getEmail());

        verify(userRepository, times(1)).findByEmail("test@test.com");
    }

    @Test
    @DisplayName("Test get user by email when user not found")
    void getUserByEmailWhenUserNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("test@test.com"));

        verify(userRepository, times(1)).findByEmail("test@test.com");
    }

    @Test
    @DisplayName("Test create user")
    void createUser() {
        when(userRepository.save(any(UserDB.class))).thenReturn(userDB);

        UserDto createdUser = userService.createUser(userDto);

        assertEquals("Test User", createdUser.getName());
        assertEquals("test@test.com", createdUser.getEmail());
        assertEquals(1, createdUser.getId());

        verify(userRepository, times(2)).save(any(UserDB.class));
    }

    @Test
    @DisplayName("Test update user name")
    void updateUserNameOrEmail() {

        when(userRepository.findById(1)).thenReturn(Optional.of(userDB));
        when(userRepository.save(any(UserDB.class))).thenReturn(userDB);

        UserDto newUserDto = new UserDto();
        newUserDto.setName("Test changed User");
        newUserDto.setEmail("new@test.com");

        UserDto updatedUser = userService.updateUserNameOrEmail(1, newUserDto);

        assertNotNull(updatedUser);
        assertEquals("Test changed User", updatedUser.getName());
        assertEquals("new@test.com", updatedUser.getEmail());

        verify(userRepository, times(2)).save(any(UserDB.class));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test delete user")
    void deleteUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userDB));

        String response = userService.deleteUser(1);

        assertEquals("Пользователь с id 1 успешно удален", response);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Test get user by id")
    void getUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userDB));

        UserDto result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@test.com", result.getEmail());

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test get user by id when user not found")
    void getUserByIdWhenUserNotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(2));

        verify(userRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("Test map to Dto")
    void mapToDto() {

        UserDto result = UserService.mapToDto(userDB);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    @DisplayName("Test map to Entity")
    void mapToEntity() {

        UserDB result = UserService.mapToEntity(userDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@test.com", result.getEmail());
    }

    private void fillingDB() {
        userDto.setId(1);
        userDto.setName("Test User");
        userDto.setEmail("test@test.com");
        userDto.setOrders(Collections.emptyList());

        userDB.setId(1);
        userDB.setName("Test User");
        userDB.setEmail("test@test.com");
        userDB.setOrders(Collections.emptyList());

        userRepository.save(userDB);
    }
}