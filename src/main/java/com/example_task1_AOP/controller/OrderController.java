package com.example_task1_AOP.controller;

import com.example_task1_AOP.annotation.SuccessLogging;
import com.example_task1_AOP.dto.OrderDto;
import com.example_task1_AOP.exception.OrderNotFoundException;
import com.example_task1_AOP.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с заказами
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SuccessLogging
public class OrderController {

    private final OrderService orderService;

    /**
     * Метод get запроса для получения заказа по id
     * Пример запроса: GET http://localhost:8080/api/orders/1
     * Пример ответа: {
     *     "id": 1,
     *     "description": "Pizza",
     *     "status": "CREATED",
     *     "userDB": "FirstUser"
     * }
     * @param id
     * @return ResponseEntity
     */

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(id));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Метод get запроса для получения всех заказов
     * Пример запроса: GET http://localhost:8080/api/orders
     * Пример ответа: [
     *     {
     *         "id": 1,
     *         "description": "Pizza",
     *         "status": "CREATED",
     *         "userDB": "FirstUser"
     *     },
     *     {
     *         "id": 2,
     *         "description": "MilkShake",
     *         "status": "IN_PROGRESS",
     *         "userDB": "SecondUser"
     *     },
     *     {
     *         "id": 3,
     *         "description": "Hamburger",
     *         "status": "COMPLETED",
     *         "userDB": "ThirdUser"
     *     }
     * ]
     * @return ResponseEntity
     */

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    /**
     * Метод post запроса для создания заказа
     * Пример запроса: POST http://localhost:8080/api/orders в формате JSON. Добавление заказа выполняется для конкретного пользователя, который находится по уникальному email.
     * {
     *     "description": "Beer",
     *     "status": "CREATED",
     *     "userDB": "111abc@abc.com"
     * }
     * Пример ответа:
     *{
     *     "id": 4,
     *     "description": "Beer",
     *     "status": "CREATED",
     *     "userDB": "FirstUser"
     * }
     *
     * @param orderDto
     * @return ResponseEntity
     */

    @PostMapping(value = "/orders", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OrderDto> createOrderForUser(@RequestBody OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.addOrderForUser(orderDto));
    }

    /**
     * Метод put запроса для обновления статуса заказа
     * Пример запроса: PUT http://localhost:8080/api/orders в формате JSON. Обновление статуса выполняется для конкретного пользователя, который находится по id заказа и уникальному email.
     *{
     *     "id": "1",
     *     "status": "COMPLETED",
     *     "userDB": "111abc@abc.com"
     * }
     * Пример ответа:
     * {
     *     "id": 1,
     *     "description": "Pizza",
     *     "status": "COMPLETED",
     *     "userDB": "FirstUser"
     * }
     * @param orderDto
     * @return ResponseEntity
     */

    @PutMapping("/orders")
    public ResponseEntity<OrderDto> updateOrderStatus(@RequestBody OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.updateOrder(orderDto));
    }
}
