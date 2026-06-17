package com.binit.flightrewards;

import com.binit.flightrewards.dto.LoginRequest;
import com.binit.flightrewards.dto.LoginResponse;
import com.binit.flightrewards.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final AuthService authService =
            new AuthService();

    @Test
    void shouldAuthenticateValidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("Password1");

        LoginResponse response =
                authService.authenticate(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals(
                "Bearer",
                response.getTokenType()
        );
        assertEquals(
                3600,
                response.getExpiresIn()
        );
    }

    @Test
    void shouldThrowWhenRequestIsNull() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.authenticate(null)
                );

        assertEquals(
                "Request cannot be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenEmailIsMissing() {

        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("Password1");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.authenticate(request)
                );

        assertEquals(
                "Email is mandatory",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenPasswordIsMissing() {

        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.authenticate(request)
                );

        assertEquals(
                "Password is mandatory",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowForInvalidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setPassword("wrong");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.authenticate(request)
                );

        assertEquals(
                "Invalid credentials",
                exception.getMessage()
        );
    }
}