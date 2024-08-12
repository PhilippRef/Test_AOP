package com.example_task1_AOP.controller;

import com.example_task1_AOP.annotation.SuccessLogging;
import com.example_task1_AOP.annotation.Throw;
import com.example_task1_AOP.dto.UserDto;
import com.example_task1_AOP.exception.UserEmailInvalidException;
import com.example_task1_AOP.exception.UserNotFoundException;
import com.example_task1_AOP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


/**
 * Контроллер для работы с пользователями
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SuccessLogging
@Throw
public class UserController {

    private final UserService userService;

    /**
     * Метод get запроса для получения всех пользователей
     * Пример запроса: GET http://localhost:8080/api/users
     * Пример ответа:
     * [
     * {
     * "id": 1,
     * "name": "FirstUser",
     * "email": "111abc@abc.com",
     * "orders": [
     * {
     * "id": 1,
     * "description": "Pizza",
     * "status": "CREATED",
     * "userDB": "FirstUser"
     * }
     * ]
     * },
     * {
     * "id": 2,
     * "name": "SecondUser",
     * "email": "222abc@abc.com",
     * "orders": [
     * {
     * "id": 2,
     * "description": "MilkShake",
     * "status": "IN_PROGRESS",
     * "userDB": "SecondUser"
     * }
     * ]
     * },
     * {
     * "id": 3,
     * "name": "ThirdUser",
     * "email": "333abc@abc.com",
     * "orders": [
     * {
     * "id": 3,
     * "description": "Hamburger",
     * "status": "COMPLETED",
     * "userDB": "ThirdUser"
     * }
     * ]
     * }
     * ]
     *
     * @return ResponseEntity
     */

    @GetMapping("/users")
    public ResponseEntity<Collection<UserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getALlUsers());
    }

    /**
     * Метод get запроса для получения пользователя по id
     * Пример запроса: GET http://localhost:8080/api/users/id/1
     * Пример ответа:
     * {
     * "id": 1,
     * "name": "FirstUser",
     * "email": "111abc@abc.com",
     * "orders": [
     * {
     * "id": 1,
     * "description": "Pizza",
     * "status": "CREATED",
     * "userDB": "FirstUser"
     * }
     * ]
     * }
     *
     * @param id
     * @return ResponseEntity
     */

    @GetMapping("/users/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Метод get запроса для получения пользователя по email
     * Пример запроса: GET http://localhost:8080/api/users/email/111abc@abc.com
     * Пример ответа:
     * {
     * "id": 1,
     * "name": "FirstUser",
     * "email": "111abc@abc.com",
     * "orders": [
     * {
     * "id": 1,
     * "description": "Pizza",
     * "status": "CREATED",
     * "userDB": "FirstUser"
     * }
     * ]
     * }
     *
     * @param email
     * @return
     */

    @GetMapping("/users/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email));
        } catch (UserEmailInvalidException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Метод post запроса для создания пользователя
     * Пример запроса: POST http://localhost:8080/api/users
     * {
     * "name": "User3",
     * "email": "988abc@abc.com",
     * "orders": [
     * {
     * }
     * ]
     * }
     * Пример ответа:
     * {
     * "id": 6,
     * "name": "User3",
     * "email": "988abc@abc.com",
     * "orders": null
     * }
     *
     * @param userDto
     * @return
     */

    @PostMapping(value = "/users", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

//    @PutMapping("/users")
//    public ResponseEntity<UserDto> updateUserInfo(@RequestBody UserDto userDto) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserName(userDto));
//    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUserInfo(@PathVariable int id,
                                                  @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUserNameOrEmail(id, userDto));
    }

    /**
     * Метод delete запроса для удаления пользователя
     * Пример запроса: DELETE http://localhost:8080/api/users/id/1
     * Пример ответа: Пользователь с id 1 успешно удален
     *
     * @param id
     * @return ResponseEntity
     */

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

