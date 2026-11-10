package com.roombooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    @GetMapping("/api/test/protected")
    public String protectedApi() {
        return "JWT Authentication Successful!";
    }
}
