package com.example_task1_AOP.dto;

import com.example_task1_AOP.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс DTO для работы с заказами
 */

@Getter
@Setter
@ToString
public class OrderDto {
    private Integer id;
    private String description;
    private OrderStatus status;
    private String userDB;

    public OrderDto() {
    }

    public OrderDto(Integer id, String description, OrderStatus status, String userDB) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.userDB = userDB;
    }
}







