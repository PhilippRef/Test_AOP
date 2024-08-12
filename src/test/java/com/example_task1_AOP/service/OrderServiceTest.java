package com.example_task1_AOP.service;

import com.example_task1_AOP.dto.OrderDto;
import com.example_task1_AOP.dto.UserDto;
import com.example_task1_AOP.entity.OrderDB;
import com.example_task1_AOP.entity.UserDB;
import com.example_task1_AOP.exception.OrderNotFoundException;
import com.example_task1_AOP.model.OrderStatus;
import com.example_task1_AOP.repositories.OrderRepository;
import com.example_task1_AOP.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private final OrderRepository orderRepository =
            Mockito.mock(OrderRepository.class);

    private final UserService userService =
            Mockito.mock(UserService.class);

    private final UserRepository userRepository =
            Mockito.mock(UserRepository.class);

    private final OrderService orderService =
            new OrderService(orderRepository, userService);

    private OrderDB orderDB;
    private UserDB userDB;
    private OrderDto orderDto;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        orderDB = new OrderDB();
        userDB = new UserDB();
        orderDto = new OrderDto();
        userDto = new UserDto();
        fillingDB();
    }

    @Test
    @DisplayName("Test get order by id when order exists")
    void testGetOrderByIdWhenOrderExists() {
        int orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderDB));

        OrderDto result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals("Test Order", result.getDescription());
        assertEquals(OrderStatus.CREATED, result.getStatus());
        assertEquals("Test User", result.getUserDB());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Test get order by id when order does not found")
    void testGetOrderByIdWhenOrderNotFound() {
        int orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Test get all orders")
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(orderDB));

        Collection<OrderDto> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.stream()
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Заказ с id: " + 1 + " не найден."))
                .getId());
        assertEquals("Test Order", result.stream()
                .findFirst()
                .orElseThrow()
                .getDescription());
        assertEquals(OrderStatus.CREATED, result.stream()
                .findFirst()
                .orElseThrow()
                .getStatus());
        assertEquals("Test User", result.stream()
                .findFirst()
                .orElseThrow()
                .getUserDB());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test add order for user")
    public void testAddOrderForUser() {
        when(userService.getUserByEmail("test@test.com")).thenReturn(userDto);
        when(orderRepository.save(any(OrderDB.class))).thenReturn(orderDB);

        OrderDto newOrder = orderService.addOrderForUser(orderDto);

        assertEquals(1, newOrder.getId());
        assertEquals("Test Order", newOrder.getDescription());
        assertEquals(OrderStatus.CREATED, newOrder.getStatus());
        assertEquals("Test User", newOrder.getUserDB());

        verify(orderRepository, times(2)).save(any(OrderDB.class));
        verify(userService, times(1)).getUserByEmail("test@test.com");
    }

    @Test
    @DisplayName("Test update order")
    void testUpdateOrder() {
        OrderDB updatedOrderDB = new OrderDB();

        updatedOrderDB.setId(1);
        updatedOrderDB.setDescription("Updated Order");
        updatedOrderDB.setStatus(OrderStatus.COMPLETED);
        updatedOrderDB.setUserDB(userDB);

        when(userService.getUserByEmail("test@test.com")).thenReturn(userDto);
        when(orderRepository.findById(1)).thenReturn(Optional.of(orderDB));
        when(orderRepository.save(any(OrderDB.class))).thenReturn(updatedOrderDB);

        OrderDto result = orderService.updateOrder(orderDto);

        assertEquals(1, result.getId());
        assertEquals("Updated Order", result.getDescription());
        assertEquals(OrderStatus.COMPLETED, result.getStatus());
        assertEquals("Test User", result.getUserDB());

        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(2)).save(any(OrderDB.class));
        verify(userService, times(1)).getUserByEmail("test@test.com");
    }

        @Test
        @DisplayName("Test map to Entity")
        void testMapToEntity () {
            OrderDB result = OrderService.mapToEntity(orderDto, userDB);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("Test Order", result.getDescription());
            assertEquals(OrderStatus.CREATED, result.getStatus());
        }

        @Test
        @DisplayName("Test map to Dto")
        void testMapToDto () {
            OrderDto result = OrderService.mapToDto(orderDB);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("Test Order", result.getDescription());
            assertEquals(OrderStatus.CREATED, result.getStatus());
            assertEquals("Test User", result.getUserDB());
        }

        private void fillingDB () {
            userDB.setId(1);
            userDB.setName("Test User");
            userDB.setEmail("test@test.com");
            userDB.setOrders(Collections.emptyList());

            userDto.setId(1);
            userDto.setName("Test User");
            userDto.setEmail("test@test.com");
            userDto.setOrders(Collections.emptyList());

            userRepository.save(userDB);

            orderDB.setId(1);
            orderDB.setDescription("Test Order");
            orderDB.setStatus(OrderStatus.CREATED);
            orderDB.setUserDB(userDB);

            orderDto.setId(1);
            orderDto.setDescription("Test Order");
            orderDto.setStatus(OrderStatus.CREATED);
            orderDto.setUserDB("test@test.com");
//        orderDto.setUserDB(userDB.getName());

            orderRepository.save(orderDB);
        }
    }