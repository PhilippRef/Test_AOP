package com.example_task1_AOP.repositories;

import com.example_task1_AOP.entity.OrderDB;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с данными о заказах
 */

public interface OrderRepository extends JpaRepository<OrderDB, Integer> {
}
