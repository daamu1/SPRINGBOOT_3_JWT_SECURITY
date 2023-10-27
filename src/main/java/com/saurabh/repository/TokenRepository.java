package com.saurabh.repository;


import com.saurabh.model.Token;
import com.saurabh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Modifying
    int deleteByUser(User user);
    Optional<Token> findByToken(String token);
}