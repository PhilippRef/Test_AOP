package com.example_task1_AOP.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspect класс для логирования OrderService и UserService.
 */

@Component
@Aspect
@Order(4)
public class LoggingAspect {

    private static final Logger log = LogManager.getLogger(LoggingAspect.class);

    /**
     * Метод позволяет логировать методы в OrderService до и после выполнения.
     * Пример вывода до выполнения метода: INFO  [http-nio-8080-exec-5] c.e.a.LoggingAspect.around(31): Выполнение метода getOrderById из OrderService с аргументами [1]
     * Пример вывода после выполнения метода: INFO  [http-nio-8080-exec-5] c.e.a.LoggingAspect.around(35): Метод getOrderById из OrderService выполнился с результатом OrderDto(id=1, description=Pizza, status=CREATED, userDB=FirstUser)
     * @param joinPoint
     * @return Object
     * @throws Throwable
     */

    @Around("bean(orderService)")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Выполнение метода {} из OrderService с аргументами {}", methodName, args);

        Object result = joinPoint.proceed(args);

        log.info("Метод {} из OrderService выполнился с результатом {}", methodName, result);

        return result;
    }

    /**
     * Это срез, запрос точки присоединения к методам UserService.
     */

    @Pointcut("execution(* com.example_task1_AOP.service.UserService.*(..))")
    public void userServicePointcut() {
    }

    /**
     * Данный метод выводит информацию о названии метода и аргументах в UserService перед их вызовом.
     * @param joinPoint
     */

    @Before("userServicePointcut()")
    public void beforeCallUserService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Начало выполнения метода {} из UserService с аргументами {}", methodName, args);
    }

    /**
     * Данный метод выводит информацию о названии метода и аргументах в UserService после его завершения.
     * @param joinPoint
     */

    @After("userServicePointcut()")
    public void afterCallUserService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Метод {} из UserService завершился с аргументами {}", methodName, args);
    }
}
