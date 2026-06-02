package com.binit.flightrewards.controller;

import com.binit.flightrewards.dto.LoginRequest;
import com.binit.flightrewards.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        System.out.println("EMAIL = " + request.getEmail());
        System.out.println("PASSWORD = " + request.getPassword());

        if (
                "user@example.com".equals(request.getEmail())
                        &&
                        "Password1".equals(request.getPassword())
        ) {

            return ResponseEntity.ok(
                    new LoginResponse("token-abc")
            );
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
    }
}