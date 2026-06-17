package com.binit.flightrewards;

import com.binit.flightrewards.controller.RewardQuoteController;
import com.binit.flightrewards.model.RewardBreakdown;
import com.binit.flightrewards.model.RewardQuoteRequest;
import com.binit.flightrewards.model.RewardQuoteResult;
import com.binit.flightrewards.service.RewardEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.http.HttpServerResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class RewardQuoteControllerTest {

    @Test
    void shouldReturn200ForValidRequest() {

        RewardEngineService service =
                mock(RewardEngineService.class);

        ObjectMapper mapper =
                new ObjectMapper();

        RewardQuoteController controller =
                new RewardQuoteController(
                        service,
                        mapper
                );

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
                  "bookingAmount":1000,
                  "currencyCode":"USD",
                  "membershipTier":"GOLD"
                }
                """
        );

        when(context.response()).thenReturn(response);

        when(response.putHeader(anyString(), anyString()))
                .thenReturn(response);

        when(response.setStatusCode(anyInt()))
                .thenReturn(response);

        RewardQuoteResult result =
                new RewardQuoteResult();

        result.rewardSummary =
                new RewardBreakdown();

        result.warnings =
                new ArrayList<>();

        when(service.generateRewardQuote(
                any(RewardQuoteRequest.class)
        ))
                .thenReturn(result);

        controller.handle(context);

        verify(response).setStatusCode(200);
    }

    @Test
    void shouldReturn400ForInvalidBookingAmount() {

        RewardEngineService service =
                mock(RewardEngineService.class);

        ObjectMapper mapper =
                new ObjectMapper();

        RewardQuoteController controller =
                new RewardQuoteController(
                        service,
                        mapper
                );

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
                  "bookingAmount":0,
                  "currencyCode":"USD",
                  "membershipTier":"GOLD"
                }
                """
        );

        when(context.response()).thenReturn(response);

        when(response.putHeader(anyString(), anyString()))
                .thenReturn(response);

        when(response.setStatusCode(anyInt()))
                .thenReturn(response);

        controller.handle(context);

        verify(response).setStatusCode(400);
    }
}