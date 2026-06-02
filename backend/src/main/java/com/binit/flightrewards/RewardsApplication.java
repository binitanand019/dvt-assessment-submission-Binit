package com.binit.flightrewards;

import com.binit.flightrewards.controller.LoginController;
import com.binit.flightrewards.controller.RewardQuoteController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class RewardsApplication extends AbstractVerticle {

    @Override
    public void start() {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        // Existing reward endpoint
        router.post("/v1/points/quote")
                .handler(new RewardQuoteController());

        // Login endpoint
        router.post("/login")
                .handler(new LoginController());

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);

        System.out.println("Flight Rewards Backend Started on Port 8080");
    }

    public static void main(String[] args) {

        Vertx.vertx()
                .deployVerticle(new RewardsApplication());
    }
}