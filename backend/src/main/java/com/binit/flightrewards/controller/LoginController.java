package com.binit.flightrewards.controller;

import com.binit.flightrewards.dto.LoginRequest;
import com.binit.flightrewards.dto.LoginResponse;
import com.binit.flightrewards.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles login requests.
 */

public class LoginController
        implements Handler<RoutingContext> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    LoginController.class
            );

    private final AuthService authService;
    private final ObjectMapper mapper;

    public LoginController(
            AuthService authService,
            ObjectMapper mapper
    ) {
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public void handle(
            RoutingContext context
    ) {

        try {

            LoginRequest request =
                    mapper.readValue(
                            context.body().asString(),
                            LoginRequest.class
                    );

            LoginResponse response =
                    authService.authenticate(
                            request
                    );

            LOGGER.info(
                    "Login successful for {}",
                    request.getEmail()
            );

            context.response()
                    .putHeader(
                            "Content-Type",
                            "application/json"
                    )
                    .setStatusCode(200)
                    .end(
                            mapper.writeValueAsString(
                                    response
                            )
                    );

        } catch (IllegalArgumentException ex) {

            LOGGER.warn(
                    "Login validation failed",
                    ex
            );

            context.response()
                    .setStatusCode(400)
                    .end(ex.getMessage());

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected login error",
                    ex
            );

            context.response()
                    .setStatusCode(500)
                    .end("Internal server error");
        }
    }
}