package com.binit.flightrewards.controller;

import com.binit.flightrewards.model.ApiErrorResponse;
import com.binit.flightrewards.model.RewardQuoteRequest;
import com.binit.flightrewards.model.RewardQuoteResult;
import com.binit.flightrewards.service.RewardEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * Handles reward quote generation requests.
 */
public class RewardQuoteController
        implements Handler<RoutingContext> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    RewardQuoteController.class
            );

    private final ObjectMapper mapper;

    private final RewardEngineService rewardEngineService;

    public RewardQuoteController(
            RewardEngineService rewardEngineService,
            ObjectMapper mapper
    ) {
        this.rewardEngineService =
                rewardEngineService;

        this.mapper = mapper;
    }

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

            LOGGER.info(
                    "Reward quote generated. RequestId={}",
                    requestId
            );

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

            LOGGER.warn(
                    "Validation failed. RequestId={}",
                    requestId,
                    ex
            );

            ApiErrorResponse error =
                    new ApiErrorResponse();

            error.message =
                    "Validation failed";

            error.validationErrors =
                    List.of(ex.getMessage());

            sendError(
                    context,
                    error,
                    400
            );

        } catch (Exception ex) {

            LOGGER.error(
                    "Unexpected error. RequestId={}",
                    requestId,
                    ex
            );

            ApiErrorResponse error =
                    new ApiErrorResponse();

            error.message =
                    "Unexpected server error";

            error.validationErrors =
                    new ArrayList<>();

            sendError(
                    context,
                    error,
                    500
            );
        }
    }

    private void validateRequest(
            RewardQuoteRequest request
    ) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Request body is mandatory"
            );
        }

        if (request.getBookingAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Booking amount must be greater than zero"
            );
        }

        if (request.getCurrencyCode() == null ||
                request.getCurrencyCode().isBlank()) {

            throw new IllegalArgumentException(
                    "Currency code is mandatory"
            );
        }

        if (request.getMembershipTier() == null ||
                request.getMembershipTier().isBlank()) {

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