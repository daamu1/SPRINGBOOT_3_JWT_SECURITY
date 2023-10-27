package com.saurabh.dto;

import com.saurabh.enums.UserRole;
import com.saurabh.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationResponseDTO {
    String token;
    String type = "Bearer";
    String refreshToken;
    Long id;
    String email;
    Set<UserRole> roles;
@Builder
    public RegistrationResponseDTO(String token, String type, String refreshToken, Long id, String email, Set<UserRole> roles) {
        this.token = token;
        this.type = type;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
