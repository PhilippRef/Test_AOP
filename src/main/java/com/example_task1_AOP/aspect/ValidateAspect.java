package com.example_task1_AOP.aspect;

import com.example_task1_AOP.exception.UserEmailInvalidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Класс для валидации
 */

@Component
@Aspect
@Order(3)
public class ValidateAspect {

    private static final Logger log = LogManager.getLogger(ValidateAspect.class);

    /**
     * Это срез, запрос точки присоединения к методам UserService.
     */

    @Pointcut("execution(* com.example_task1_AOP.service.UserService.*(..))")
    public void needValidatePointcut() {
    }

    /**
     * Это срез, запрос точки присоединения к методам UserService, указанным выше с аргументом.
     * @param email
     */

    @Pointcut("needValidatePointcut() && args(email)")
    public void validateEmailPointcut(String email) {
    }

    /**
     * Метод валидирует email
     * @param email
     */

    @Before(value = "validateEmailPointcut(email)", argNames = "email")
    public void validateEmail(String email) {
        log.info("Валидация email: {}", email);
        validate(email);
    }

    /**
     * Приватный метод, в котором указана логика валидации email.
     * В случае некорректного email выбрасывается исключение UserEmailInvalidException с указанием не корректного email.
     * @param email
     */

    private void validate(String email) {
        if (!email.matches("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}")) {
            throw new UserEmailInvalidException("Введен не корректный email: " + email);
        }
    }
}
