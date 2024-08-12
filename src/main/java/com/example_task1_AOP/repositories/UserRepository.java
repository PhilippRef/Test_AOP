package com.example_task1_AOP.repositories;

import com.example_task1_AOP.entity.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Репозиторий для работы с данными о пользователях
 */

public interface UserRepository extends JpaRepository<UserDB, Integer> {
    @Query("SELECT p FROM UserDB p WHERE p.email = ?1")
    UserDB findByEmail(String email);
}
