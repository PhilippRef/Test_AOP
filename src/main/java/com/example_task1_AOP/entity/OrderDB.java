package com.example_task1_AOP.entity;

import com.example_task1_AOP.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс для хранения данных о заказах с помощью ORM
 */

@Entity
@Table(name = "orders")
@Setter
@Getter
@ToString
public class OrderDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_Id", nullable = false)
    private Integer id;

    @Column(name = "order_description", nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = true, columnDefinition = "enum('CREATED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')")
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDB userDB;

    public OrderDB() {
    }

    public OrderDB(Integer id, String description, OrderStatus status, UserDB userDB) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.userDB = userDB;
    }
}
