package com.binit.flightrewards;

import com.binit.flightrewards.controller.LoginController;
import com.binit.flightrewards.dto.LoginRequest;
import com.binit.flightrewards.dto.LoginResponse;
import com.binit.flightrewards.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.http.HttpServerResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Test
    void shouldReturn200ForValidLogin() throws Exception {

        AuthService authService = mock(AuthService.class);

        ObjectMapper mapper = new ObjectMapper();

        LoginController controller =
                new LoginController(authService, mapper);

        RoutingContext context =
                mock(RoutingContext.class);

        RequestBody body =
                mock(RequestBody.class);

        HttpServerResponse response =
                mock(HttpServerResponse.class);

        when(context.body()).thenReturn(body);

        when(body.asString()).thenReturn(
                """
                {
                  "email":"user@example.com",
                  "password":"Password1"
                }
                """
        );

        when(context.response()).thenReturn(response);

        when(response.putHeader(anyString(), anyString()))
                .thenReturn(response);

        when(response.setStatusCode(anyInt()))
                .thenReturn(response);

        when(authService.authenticate(any(LoginRequest.class)))
                .thenReturn(
                        new LoginResponse(
                                "token",
                                3600,
                                "Bearer"
                        )
                );

        controller.handle(context);

        verify(response).setStatusCode(200);
    }

    @Test
    void shouldReturn400ForInvalidCredentials() {

        AuthService authService = mock(AuthService.class);

        ObjectMapper mapper = new ObjectMapper();

        LoginController controller =
                new LoginController(authService, mapper);

        RoutingContext context =
                mock(RoutingContext.class);

        RequestBody body =
                mock(RequestBody.class);

        HttpServerResponse response =
                mock(HttpServerResponse.class);

        when(context.body()).thenReturn(body);

        when(body.asString()).thenReturn(
                """
                {
                  "email":"bad@test.com",
                  "password":"bad"
                }
                """
        );

        when(context.response()).thenReturn(response);

        when(response.setStatusCode(anyInt()))
                .thenReturn(response);

        doThrow(
                new IllegalArgumentException(
                        "Invalid credentials"
                )
        )
                .when(authService)
                .authenticate(any());

        controller.handle(context);

        verify(response).setStatusCode(400);
    }
}