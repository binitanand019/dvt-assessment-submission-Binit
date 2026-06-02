package com.binit.flightrewards.controller;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class LoginController implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {

        JsonObject body = context.body().asJsonObject();

        String email = body.getString("email");
        String password = body.getString("password");

        System.out.println("EMAIL = " + email);
        System.out.println("PASSWORD = " + password);

        if (
                "user@example.com".equals(email)
                        &&
                        "Password1".equals(password)
        ) {

            JsonObject response = new JsonObject()
                    .put("token", "token-abc");

            context.response()
                    .putHeader("Content-Type", "application/json")
                    .setStatusCode(200)
                    .end(response.encode());

        } else {

            context.response()
                    .setStatusCode(401)
                    .end("Invalid credentials");
        }
    }
}