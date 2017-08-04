package com.mitchtalmadge.apps.discord.professor_doge.crypto.cache;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
class ExchangeTickerCacheUpdater {

    private final LogService logService;

    @Autowired
    public ExchangeTickerCacheUpdater(LogService logService) {
        this.logService = logService;
    }

    /**
     * Updates the Tickers for a specific Exchange.
     *
     * @param reference The reference to the Exchange.
     * @return The cached Tickers.
     */
    @SuppressWarnings("WeakerAccess")
    @Async
    public CompletableFuture<Map<CurrencyPair, Ticker>> updateExchangeCache(ExchangeReference reference) {
        // Create a cache for the Exchange.
        Map<CurrencyPair, Ticker> exchangeCache = new HashMap<>();

        // Get the MarketDataService for the Exchange.
        MarketDataService marketDataService = reference.getMarketDataService();

        // Try the Currency Pairs.
        for (CurrencyPair currencyPair : TickerCacheService.CURRENCY_PAIRS) {
            Ticker ticker = null;

            // Try to get the Ticker for this CurrencyPair.
            try {
                ticker = marketDataService.getTicker(currencyPair);
            } catch (Exception ignored) {
            }

            // If the Ticker was found, add it to the cache.
            if (ticker != null) {
                exchangeCache.put(currencyPair, ticker);
                logService.logDebug(getClass(), reference.name() + ": " + currencyPair.toString() + ": Success");
            } else {
                logService.logDebug(getClass(), reference.name() + ": " + currencyPair.toString() + ": Fail");
            }
        }

        return CompletableFuture.completedFuture(exchangeCache);
    }

}