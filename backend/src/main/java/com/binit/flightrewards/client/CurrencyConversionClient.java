package com.binit.flightrewards.client;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

public class CurrencyConversionClient {

    private final WebClient webClient;

    public CurrencyConversionClient() {

        this.webClient =
                WebClient.create(Vertx.vertx());
    }

    public double fetchFxRate(String currencyCode) {

        int retryCount = 0;

        while (retryCount < 3) {

            try {

                System.out.println(
                        "Fetching FX rate attempt: "
                                + (retryCount + 1)
                );

                if ("USD".equalsIgnoreCase(currencyCode)) {
                    return 1.0;
                }

                if ("INR".equalsIgnoreCase(currencyCode)) {
                    return 0.012;
                }

                if ("EUR".equalsIgnoreCase(currencyCode)) {
                    return 1.08;
                }

                Thread.sleep(200);

                return 1.0;

            } catch (Exception ex) {

                retryCount++;

                System.out.println(
                        "Retrying FX lookup..."
                );
            }
        }

        System.out.println(
                "Fallback FX rate applied"
        );

        return 1.0;
    }
}