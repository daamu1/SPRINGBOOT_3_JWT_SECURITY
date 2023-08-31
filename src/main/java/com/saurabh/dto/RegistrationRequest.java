package com.saurabh.dto;

import com.saurabh.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    Role role;
}