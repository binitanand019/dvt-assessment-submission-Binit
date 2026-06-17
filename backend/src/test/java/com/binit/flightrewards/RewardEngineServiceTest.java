package com.binit.flightrewards;

import com.binit.flightrewards.client.CurrencyConversionClient;
import com.binit.flightrewards.model.RewardQuoteRequest;
import com.binit.flightrewards.model.RewardQuoteResult;
import com.binit.flightrewards.service.RewardEngineService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RewardEngineServiceTest {

    @Test
    void shouldGenerateRewardsForSilverMember() {

        CurrencyConversionClient client =
                new CurrencyConversionClient();

        RewardEngineService service =
                new RewardEngineService(client);

        RewardQuoteRequest request =
                new RewardQuoteRequest();

        request.setBookingAmount(1000);
        request.setCurrencyCode("USD");
        request.setMembershipTier("SILVER");

        RewardQuoteResult result =
                service.generateRewardQuote(request);

        assertNotNull(result);
        assertNotNull(result.rewardSummary);

        assertEquals(
                1000,
                result.rewardSummary.baseRewards
        );

        assertEquals(
                150,
                result.rewardSummary.tierRewards
        );

        assertEquals(
                250,
                result.rewardSummary.campaignRewards
        );

        assertEquals(
                1400,
                result.rewardSummary.totalRewards
        );
    }

    @Test
    void shouldGenerateRewardsForGoldMember() {

        CurrencyConversionClient client =
                new CurrencyConversionClient();

        RewardEngineService service =
                new RewardEngineService(client);

        RewardQuoteRequest request =
                new RewardQuoteRequest();

        request.setBookingAmount(1000);
        request.setCurrencyCode("USD");
        request.setMembershipTier("GOLD");

        RewardQuoteResult result =
                service.generateRewardQuote(request);

        assertEquals(
                1550,
                result.rewardSummary.totalRewards
        );
    }

    @Test
    void shouldGenerateRewardsForPlatinumMember() {

        CurrencyConversionClient client =
                new CurrencyConversionClient();

        RewardEngineService service =
                new RewardEngineService(client);

        RewardQuoteRequest request =
                new RewardQuoteRequest();

        request.setBookingAmount(1000);
        request.setCurrencyCode("USD");
        request.setMembershipTier("PLATINUM");

        RewardQuoteResult result =
                service.generateRewardQuote(request);

        assertEquals(
                1750,
                result.rewardSummary.totalRewards
        );
    }

    @Test
    void shouldHandleUnknownTier() {

        CurrencyConversionClient client =
                new CurrencyConversionClient();

        RewardEngineService service =
                new RewardEngineService(client);

        RewardQuoteRequest request =
                new RewardQuoteRequest();

        request.setBookingAmount(1000);
        request.setCurrencyCode("USD");
        request.setMembershipTier("UNKNOWN");

        RewardQuoteResult result =
                service.generateRewardQuote(request);

        assertEquals(
                1250,
                result.rewardSummary.totalRewards
        );
    }

    @Test
    void shouldApplyRewardCap() {

        CurrencyConversionClient client =
                new CurrencyConversionClient();

        RewardEngineService service =
                new RewardEngineService(client);

        RewardQuoteRequest request =
                new RewardQuoteRequest();

        request.setBookingAmount(50000);
        request.setCurrencyCode("USD");
        request.setMembershipTier("PLATINUM");

        RewardQuoteResult result =
                service.generateRewardQuote(request);

        assertEquals(
                50000,
                result.rewardSummary.totalRewards
        );

        assertFalse(
                result.warnings.isEmpty()
        );

        assertEquals(
                "Maximum reward cap applied",
                result.warnings.get(0)
        );
    }
}