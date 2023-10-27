package com.saurabh.model;


import com.saurabh.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    @GeneratedValue
    Long id;
    @Column(unique = true, nullable = false)
    String token;
    @Enumerated(EnumType.STRING)
    TokenType tokenType;
    boolean revoked;
    boolean expired;
    @Column(nullable = false)
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;
}