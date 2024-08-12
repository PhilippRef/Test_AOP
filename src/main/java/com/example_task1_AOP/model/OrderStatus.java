package com.example_task1_AOP.model;

import lombok.Getter;

/**
 * Класс для хранения статусов заказов
 */

@Getter
public enum OrderStatus {

    CREATED("CREATED"), IN_PROGRESS("IN_PROGRESS"), COMPLETED("COMPLETED"), CANCELLED("CANCELLED");

    private final String status;

    private OrderStatus(String status) {
        this.status = status;
    }
}
