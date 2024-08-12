package com.example_task1_AOP.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Класс DTO для работы с пользователями
 */

@Getter
@Setter
@ToString
public class UserDto {

    private Integer id;
    private String name;
    private String email;
    private List<OrderDto> orders;

    public UserDto() {
    }

    public UserDto(Integer id, String name, String email, List<OrderDto> orders) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.orders = orders;
    }
}



