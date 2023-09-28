package com.saurabh.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo-controller")
@Hidden
public class TestController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @GetMapping("/msg")
    public ResponseEntity<Map<String,String>> message()
    {
        Map<String,String>message=new HashMap<>();
        message.put("Message","Hey I am inside the Test controller");
        return ResponseEntity.ok(message);
    }
}