package com.example_task1_AOP.exception;

import com.example_task1_AOP.annotation.Throw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для вызова исключения при отсутствии корректного электронного адреса
 */

@Throw
public class UserEmailInvalidException extends RuntimeException {

    private static final Logger log = LogManager.getLogger(UserEmailInvalidException.class);

    public UserEmailInvalidException() {
    }

    /**
     * Конструктор вызывается при вызове исключения UserEmailInvalidException
     * Пример вывода: ERROR [http-nio-8080-exec-7] c.e.e.UserEmailInvalidException.<init>(17): Введен не корректный email: fgr@dfd@.com
     * @param message
     */

    public UserEmailInvalidException(String message) {
        super(message);
        log.error(message);
    }
}
