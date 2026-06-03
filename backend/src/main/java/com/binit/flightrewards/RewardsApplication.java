package com.binit.flightrewards;

import com.binit.flightrewards.client.CurrencyConversionClient;
import com.binit.flightrewards.controller.LoginController;
import com.binit.flightrewards.controller.RewardQuoteController;
import com.binit.flightrewards.service.AuthService;
import com.binit.flightrewards.service.RewardEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RewardsApplication extends AbstractVerticle {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    RewardsApplication.class
            );

    @Override
    public void start() {

        Router router = Router.router(vertx);

        router.route()
                .handler(BodyHandler.create());

        ObjectMapper mapper =
                new ObjectMapper();

        CurrencyConversionClient currencyClient =
                new CurrencyConversionClient();

        RewardEngineService rewardEngineService =
                new RewardEngineService(
                        currencyClient
                );

        AuthService authService =
                new AuthService();

        router.post("/v1/points/quote")
                .handler(
                        new RewardQuoteController(
                                rewardEngineService,
                                mapper
                        )
                );

        router.post("/login")
                .handler(
                        new LoginController(
                                authService,
                                mapper
                        )
                );

        int port =
                Integer.parseInt(
                        System.getenv()
                                .getOrDefault(
                                        "PORT",
                                        "8080"
                                )
                );

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port);

        LOGGER.info(
                "Flight Rewards Backend Started on Port {}",
                port
        );
    }

    public static void main(String[] args) {

        Vertx.vertx()
                .deployVerticle(
                        new RewardsApplication()
                );
    }
}