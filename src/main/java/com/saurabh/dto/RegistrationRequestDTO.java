package com.saurabh.dto;

import com.saurabh.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequestDTO {
    String userName;
    String email;
    String password;
    Set<UserRole> role;
}