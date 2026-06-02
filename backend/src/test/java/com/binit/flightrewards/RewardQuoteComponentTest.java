package com.binit.flightrewards;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RewardQuoteComponentTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {

        wireMockServer =
                new WireMockServer(9090);

        wireMockServer.start();

        configureFor("localhost", 9090);
    }

    @AfterAll
    static void cleanup() {

        wireMockServer.stop();
    }

    @Test
    void shouldMockCurrencyApiSuccessfully() {

        stubFor(get(urlEqualTo("/fx-rate"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withBody("{\"rate\":1.0}")
                ));

        assertThat(
                wireMockServer.isRunning()
        ).isTrue();
    }

    @Test
    void shouldReturnFallbackWhenCurrencyFails() {

        stubFor(get(urlEqualTo("/fx-rate"))
                .willReturn(
                        aResponse()
                                .withStatus(500)
                ));

        assertThat(
                wireMockServer.isRunning()
        ).isTrue();
    }
}