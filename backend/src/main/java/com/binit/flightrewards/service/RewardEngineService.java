package com.binit.flightrewards.service;

import com.binit.flightrewards.client.CurrencyConversionClient;
import com.binit.flightrewards.model.RewardBreakdown;
import com.binit.flightrewards.model.RewardQuoteRequest;
import com.binit.flightrewards.model.RewardQuoteResult;

import java.time.Instant;
import java.util.ArrayList;

public class RewardEngineService {

    private final CurrencyConversionClient
            currencyConversionClient =
            new CurrencyConversionClient();

    public RewardQuoteResult generateRewardQuote(
            RewardQuoteRequest request
    ) {

        RewardQuoteResult result = new RewardQuoteResult();

        RewardBreakdown breakdown = new RewardBreakdown();

        double fxRate =
                currencyConversionClient
                        .fetchFxRate(request.currencyCode);

        int baseRewards =
                (int) (request.bookingAmount * fxRate);

        int tierRewards = calculateTierRewards(
                baseRewards,
                request.membershipTier
        );

        int campaignRewards = calculateCampaignRewards(baseRewards);

        int totalRewards =
                baseRewards +
                        tierRewards +
                        campaignRewards;

        if (totalRewards > 50000) {
            totalRewards = 50000;
        }

        breakdown.baseRewards = baseRewards;
        breakdown.tierRewards = tierRewards;
        breakdown.campaignRewards = campaignRewards;
        breakdown.totalRewards = totalRewards;

        result.rewardSummary = breakdown;

        result.generatedAt = Instant.now().toString();

        result.warnings = new ArrayList<>();

        if (totalRewards >= 50000) {

            result.warnings.add(
                    "Maximum reward cap applied"
            );
        }

        return result;
    }

    private int calculateTierRewards(
            int baseRewards,
            String tier
    ) {

        if (tier == null) {
            return 0;
        }

        return switch (tier.toUpperCase()) {

            case "SILVER" ->
                    (int) (baseRewards * 0.15);

            case "GOLD" ->
                    (int) (baseRewards * 0.30);

            case "PLATINUM" ->
                    (int) (baseRewards * 0.50);

            default -> 0;
        };
    }

    private int calculateCampaignRewards(int baseRewards) {

        return (int) (baseRewards * 0.25);
    }
}