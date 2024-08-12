package com.example_task1_AOP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Класс для хранения данных о пользователях с помощью ORM
 */

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class UserDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "userDB", cascade = CascadeType.ALL)
    private List<OrderDB> orders;

    public UserDB() {
    }

    public UserDB(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
