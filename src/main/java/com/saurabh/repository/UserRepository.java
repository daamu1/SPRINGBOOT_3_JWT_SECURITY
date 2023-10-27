package com.saurabh.repository;


import java.util.Optional;

import com.saurabh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//    Optional<User> findByUsername(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);
}