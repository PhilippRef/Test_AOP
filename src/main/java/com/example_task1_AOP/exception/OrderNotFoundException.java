package com.example_task1_AOP.exception;

import com.example_task1_AOP.annotation.Throw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для вызова исключения при отсутствии заказа
 */

@Throw
public class OrderNotFoundException extends RuntimeException {

    private static final Logger log = LogManager.getLogger(OrderNotFoundException.class);

    public OrderNotFoundException() {
    }

    /**
     * Конструктор вызывается при возникновении исключения OrderNotFoundException
     * Пример вывода: ERROR [http-nio-8080-exec-2] c.e.e.OrderNotFoundException.<init>(26): Заказ с id: 99 не найден.
     * @param message
     */
    public OrderNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
