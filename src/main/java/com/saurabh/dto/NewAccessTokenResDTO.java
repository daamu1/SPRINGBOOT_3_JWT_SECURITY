package com.saurabh.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewAccessTokenResDTO {
     String accessToken;
     String refreshToken;
     String tokenType = "Bearer";

     public NewAccessTokenResDTO(String token, String refreshToken) {
          this.accessToken=token;
          this.refreshToken=refreshToken;
          this.tokenType="Bearer";
     }
}
