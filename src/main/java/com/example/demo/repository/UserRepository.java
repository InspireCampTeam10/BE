package com.example.demo.repository;

import com.example.demo.domian.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Username이 존재하는지 확인하는 JPA existBy~
    Boolean existsByUsername(String username);
}
