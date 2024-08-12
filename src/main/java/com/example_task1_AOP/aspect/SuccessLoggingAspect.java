package com.example_task1_AOP.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Класс для логирования успешного выполнения методов из контроллеров
 */

@Component
@Aspect
@Order(1)
public class SuccessLoggingAspect {

    private static final Logger log = LogManager.getLogger(SuccessLoggingAspect.class);

    /**
     * Метод выводит в лог успешное выполнение методов в контроллере UserController
     * Пример вывода: INFO  [http-nio-8080-exec-2] c.e.a.SuccessLoggingAspect.afterReturningUserController(33): Метод getAllUsers класса UserController успешно выполнился с аргументами: []
     * @param joinPoint
     */

    @AfterReturning("within(com.example_task1_AOP.controller.UserController) &&" +
            "@within(com.example_task1_AOP.annotation.SuccessLogging)")
    public void afterReturningUserController(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        log.info("Метод {} класса UserController успешно выполнился с аргументами: {}", methodName, args);
    }

    /**
     * Метод выводит в лог успешное выполнение методов в контроллере OrderController
     * Пример вывода: INFO  [http-nio-8080-exec-5] c.e.a.SuccessLoggingAspect.afterReturningOrderController(47): Метод getAllOrders класса OrderController успешно выполнился с аргументами: []
     * @param joinPoint
     */

    @AfterReturning("within(com.example_task1_AOP.controller.OrderController) &&" +
            "@within(com.example_task1_AOP.annotation.SuccessLogging)")
    public void afterReturningOrderController(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        log.info("Метод {} класса OrderController успешно выполнился с аргументами: {}", methodName, args);
    }


}
