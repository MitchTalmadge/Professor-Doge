package com.mitchtalmadge.apps.discord.professor_doge.crypto.cron;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.CryptocurrencyService;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SpecificTickerCacher {

    private final CryptocurrencyService cryptocurrencyService;
    private final LogService logService;

    @Autowired
    public SpecificTickerCacher(CryptocurrencyService cryptocurrencyService,
                                LogService logService) {
        this.cryptocurrencyService = cryptocurrencyService;
        this.logService = logService;
    }

    /**
     * Updates the prices for a specific Exchange and stores them within the {@link CryptocurrencyService} Ticker Cache.
     *
     * @param reference The reference to the Exchange.
     */
    @SuppressWarnings("WeakerAccess")
    @Async
    public void updatePricesForExchange(ExchangeReference reference) {
        // Create a cache for the Exchange.
        Map<CurrencyPair, Ticker> exchangeCache = new HashMap<>();

        // Get the MarketDataService for the Exchange.
        MarketDataService marketDataService = reference.getMarketDataService();

        // Try the Currency Pairs.
        for (CurrencyPair currencyPair : GeneralTickerCacher.CURRENCY_PAIRS) {
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

        cryptocurrencyService.updateExchangeCache(reference, exchangeCache);
    }

}
