package com.example_task1_AOP.service;

import com.example_task1_AOP.dto.OrderDto;
import com.example_task1_AOP.entity.OrderDB;
import com.example_task1_AOP.entity.UserDB;
import com.example_task1_AOP.exception.OrderNotFoundException;
import com.example_task1_AOP.model.OrderStatus;
import com.example_task1_AOP.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервисный класс для обработки логики работы с заказами
 */

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    /**
     * Метод для получения заказа по id. Если такого заказа нет, выбрасывает исключение OrderNotFoundException
     *
     * @param orderId
     * @return OrderDto
     */

    public OrderDto getOrderById(int orderId) {
        Optional<OrderDB> orderDB = orderRepository.findById(orderId);
        return orderDB.map(OrderService::mapToDto)
                .orElseThrow(() -> new OrderNotFoundException("Заказ с id: " + orderId + " не найден."));
    }

    /**
     * Метод для получения всех заказов
     *
     * @return Collection<OrderDto>
     */

    public Collection<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderService::mapToDto)
                .toList();
    }

    /**
     * Метод для добавления нового заказа для конкретного пользователя
     *
     * @param orderDto
     * @return OrderDto
     */

    @Transactional
    public OrderDto addOrderForUser(OrderDto orderDto) {
        String user = orderDto.getUserDB();
        UserDB userDB = UserService.mapToEntity(userService.getUserByEmail(user));

        OrderDB orderDB = mapToEntity(orderDto, userDB);

        orderDB.setUserDB(userDB);
        orderDB.setStatus(OrderStatus.CREATED);

        OrderDB createdOrderDB = orderRepository.save(orderDB);

        return mapToDto(createdOrderDB);
    }

    /**
     * Метод для обновления существующего заказа
     *
     * @param orderDto
     * @return OrderDto
     */

    @Transactional
    public OrderDto updateOrder(OrderDto orderDto) {
        String user = orderDto.getUserDB();
        UserDB userDB = UserService.mapToEntity(userService.getUserByEmail(user));

        OrderDB orderDB = mapToEntity(orderDto, userDB);
        OrderDB orderId = mapToEntity(getOrderById(orderDto.getId()), userDB);

        String status = String.valueOf(orderDto.getStatus());
        String description = orderId.getDescription();


        orderDB.setDescription(description);
        orderDB.setStatus(OrderStatus.valueOf(status));
        orderDB.setUserDB(userDB);

        OrderDB updatedOrderDB = orderRepository.save(orderDB);

        return mapToDto(updatedOrderDB);
    }

    /**
     * Метода для перевода данных из DTO в Entity
     *
     * @param orderDto
     * @return OrderDB
     */

    public static OrderDB mapToEntity(OrderDto orderDto, UserDB userDB) {
        OrderDB orderDB = new OrderDB();

        orderDB.setId(orderDto.getId());
        orderDB.setDescription(orderDto.getDescription());
        orderDB.setStatus(orderDto.getStatus()); //TODO: здесь не присваивается userDB
        orderDB.setUserDB(userDB);

        return orderDB;
    }

    /**
     * Метод для перевода данных из Entity в DTO
     *
     * @param orderDB
     * @return OrderDto
     */

    public static OrderDto mapToDto(OrderDB orderDB) {
        OrderDto orderDto = new OrderDto();

        orderDto.setId(orderDB.getId());
        orderDto.setDescription(orderDB.getDescription());
        orderDto.setStatus(orderDB.getStatus());
        orderDto.setUserDB(orderDB.getUserDB().getName());

        return orderDto;
    }
}
