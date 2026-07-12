package com.airline.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HomeController {

    @GetMapping
    public ResponseEntity<String> home() {

        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/user")
    public ResponseEntity<String> user() {
        System.out.println("inside user controller");
        return ResponseEntity.ok("Hello World");
    }
}
