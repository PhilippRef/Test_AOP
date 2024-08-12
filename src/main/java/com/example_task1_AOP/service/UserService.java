package com.example_task1_AOP.service;

import com.example_task1_AOP.annotation.Valid;
import com.example_task1_AOP.dto.UserDto;
import com.example_task1_AOP.entity.UserDB;
import com.example_task1_AOP.exception.UserNotFoundException;
import com.example_task1_AOP.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для обработки логики работы с пользователями
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Метод для получения всех пользователей и их заказов
     *
     * @return List<UserDto></UserDto>
     */

    public List<UserDto> getALlUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserService::mapToDto)
                .toList();
    }

    /**
     * Метод для получения пользователя по email с его заказами. Если пользователь не найден выбрасывается исключение UserNotFoundException.
     * При вызове метода происходит валидация корректности введенного email. Если email не корректный выбрасывается исключение UserEmailInvalidException
     *
     * @param email
     * @return UserDto
     */

    public UserDto getUserByEmail(@Valid String email) {
        Optional<UserDB> userDB = Optional.ofNullable(userRepository.findByEmail(email));

        return userDB.map(UserService::mapToDto)
                .orElseThrow(() ->
                        new UserNotFoundException("Пользователь с email: " + email + " не найден"));
    }

    /**
     * Метод для создания нового пользователя
     *
     * @param userDto
     * @return
     */

    @Transactional
    public UserDto createUser(UserDto userDto) {
        UserDB userDB = mapToEntity(userDto);
        UserDB createUserDB = userRepository.save(userDB);

        return createAndUpdateUserMapToDto(createUserDB);
    }

    /**
     * Метод для обновления имени или email пользователя
     * @param id
     * @param userDto
     * @return UserDto
     */

    @Transactional
    public UserDto updateUserNameOrEmail(int id, UserDto userDto) {
        UserDB userDB = mapToEntity(getUserById(id));

        userDB.setName(userDto.getName());
        userDB.setEmail(userDto.getEmail());

        userRepository.save(userDB);

        return mapToDto(userDB);
    }

    /**
     * Метод для удаления пользователя
     *
     * @param id
     * @return String
     */

    @Transactional
    public String deleteUser(int id) {
        UserDB userDB = mapToEntity(getUserById(id));

        userRepository.deleteById(userDB.getId());

        return "Пользователь с id " + id + " успешно удален";
    }

    /**
     * Метод для получения пользователя по id. Если пользователь не обнаружен выбрасывается исключение UserNotFoundException.
     *
     * @param id
     * @return UserDto
     */

    public UserDto getUserById(int id) {
        Optional<UserDB> userDB = userRepository.findById(id);

        return userDB.map(UserService::mapToDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не найден"));
    }

    /**
     * Метода для перевода данных из Entity в Dto
     *
     * @param userDB
     * @return userDto
     */

    public static UserDto mapToDto(UserDB userDB) {
        UserDto userDto = new UserDto();

        userDto.setId(userDB.getId());
        userDto.setName(userDB.getName());
        userDto.setEmail(userDB.getEmail());
        userDto.setOrders(userDB.getOrders()
                .stream()
                .map(OrderService::mapToDto)
                .toList());

        return userDto;
    }

    /**
     * Метода для перевода данных из Dto в Entity
     *
     * @param userDto
     * @return userDB
     */

    public static UserDB mapToEntity(UserDto userDto) {
        UserDB userDB = new UserDB();

        userDB.setId(userDto.getId());
        userDB.setName(userDto.getName());
        userDB.setEmail(userDto.getEmail());
        userDB.setOrders(userDto.getOrders()
                .stream()
                .map(orderDto -> OrderService.mapToEntity(orderDto, userDB))
                .toList());

        return userDB;
    }

    /**
     * Метод перевода данных из Entity в Dto для создания пользователя
     *
     * @param userDB
     * @return
     */

    private UserDto createAndUpdateUserMapToDto(UserDB userDB) {
        UserDto userDto = new UserDto();

        userDto.setId(userDB.getId());
        userDto.setName(userDB.getName());
        userDto.setEmail(userDB.getEmail());

        return userDto;
    }
}
