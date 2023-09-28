package com.saurabh.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T>{
     final boolean success;
     final  String message;
     final T data;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        data = null;
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
