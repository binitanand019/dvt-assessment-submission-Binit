package com.binit.flightrewards.controller;

import com.binit.flightrewards.model.ApiErrorResponse;
import com.binit.flightrewards.model.RewardQuoteRequest;
import com.binit.flightrewards.model.RewardQuoteResult;
import com.binit.flightrewards.service.RewardEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RewardQuoteController
        implements Handler<RoutingContext> {

    private final ObjectMapper mapper =
            new ObjectMapper();

    private final RewardEngineService rewardEngineService =
            new RewardEngineService();

    @Override
    public void handle(RoutingContext context) {

        String requestId =
                UUID.randomUUID().toString();

        try {

            RewardQuoteRequest request =
                    mapper.readValue(
                            context.body().asString(),
                            RewardQuoteRequest.class
                    );

            validateRequest(request);

            RewardQuoteResult result =
                    rewardEngineService
                            .generateRewardQuote(request);

            context.response()
                    .putHeader(
                            "Content-Type",
                            "application/json"
                    )
                    .putHeader(
                            "x-request-id",
                            requestId
                    )
                    .setStatusCode(200)
                    .end(
                            mapper.writeValueAsString(result)
                    );

        } catch (IllegalArgumentException ex) {

            ApiErrorResponse error =
                    new ApiErrorResponse();

            error.message =
                    "Validation failed";

            error.validationErrors =

                    List.of(ex.getMessage());

            sendError(context, error, 400);

        } catch (Exception ex) {

            ApiErrorResponse error =
                    new ApiErrorResponse();

            error.message =
                    "Unexpected server error";

            error.validationErrors =
                    new ArrayList<>();

            sendError(context, error, 500);
        }
    }

    private void validateRequest(
            RewardQuoteRequest request
    ) {

        if (request.bookingAmount <= 0) {

            throw new IllegalArgumentException(
                    "Booking amount must be greater than zero"
            );
        }

        if (request.currencyCode == null ||
                request.currencyCode.isBlank()) {

            throw new IllegalArgumentException(
                    "Currency code is mandatory"
            );
        }

        if (request.membershipTier == null ||
                request.membershipTier.isBlank()) {

            throw new IllegalArgumentException(
                    "Membership tier is mandatory"
            );
        }
    }

    private void sendError(
            RoutingContext context,
            ApiErrorResponse error,
            int statusCode
    ) {

        try {

            context.response()
                    .putHeader(
                            "Content-Type",
                            "application/json"
                    )
                    .setStatusCode(statusCode)
                    .end(
                            mapper.writeValueAsString(error)
                    );

        } catch (Exception ex) {

            context.response()
                    .setStatusCode(500)
                    .end();
        }
    }
}