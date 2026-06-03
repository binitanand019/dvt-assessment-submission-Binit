package com.binit.flightrewards.service;

import com.binit.flightrewards.dto.LoginRequest;
import com.binit.flightrewards.dto.LoginResponse;

import java.util.UUID;

/**
 * Service responsible for
 * authentication operations.
 */

public class AuthService {

    private static final String DEMO_EMAIL =
            "user@example.com";

    private static final String DEMO_PASSWORD =
            "Password1";

    /**
     * Authenticates user credentials.
     *
     * @param request login request
     * @return authentication token
     */
    public LoginResponse authenticate(
            LoginRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Request cannot be null"
            );
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Email is mandatory"
            );
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Password is mandatory"
            );
        }

        if (!DEMO_EMAIL.equalsIgnoreCase(
                request.getEmail())
                ||
                !DEMO_PASSWORD.equals(
                        request.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid credentials"
            );
        }

        return new LoginResponse(
                UUID.randomUUID().toString(),
                3600,
                "Bearer"
        );
    }
}