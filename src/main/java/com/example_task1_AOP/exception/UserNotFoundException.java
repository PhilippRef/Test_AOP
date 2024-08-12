package com.example_task1_AOP.exception;

import com.example_task1_AOP.annotation.Throw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для вызова исключения при отсутствии запрашиваемого пользователя
 */

@Throw
public class UserNotFoundException extends RuntimeException {

    private static final Logger log = LogManager.getLogger(UserNotFoundException.class);

    public UserNotFoundException() {
    }

    /**
     * Конструктор вызывается при вызове исключения UserNotFoundException
     * Пример вывода: ERROR [http-nio-8080-exec-10] c.e.e.UserNotFoundException.<init>(17): Пользователь с id: 10 не найден
     * @param message
     */

    public UserNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
