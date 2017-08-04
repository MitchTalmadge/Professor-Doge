package com.mitchtalmadge.apps.discord.professor_doge.crypto.cache;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TickerCacheService {

    /**
     * The valid Currency bases.
     */
    private static final Currency[] BASES = new Currency[]{
            Currency.BTC,
            Currency.ETH,
            Currency.LTC,
            Currency.XRP,
            Currency.DOGE,
            new Currency("DASH")
    };

    /**
     * The valid Currency counters.
     */
    private static final Currency[] COUNTERS = new Currency[]{
            Currency.USD,
            Currency.EUR,
            Currency.GBP,
            Currency.BTC,
            Currency.ETH,
            Currency.LTC
    };

    /**
     * The valid CurrencyPairs, consisting of all combinations of BASES and COUNTERS.
     */
    static final Set<CurrencyPair> CURRENCY_PAIRS = new HashSet<>();

    /**
     * The cache containing a reference to each Exchange, their currency pairs, and their respective tickets.
     */
    private static final Map<ExchangeReference, Map<CurrencyPair, Ticker>> TICKER_CACHE = new ConcurrentHashMap<>();

    static {
        // Initialize the cache.
        for (ExchangeReference reference : ExchangeReference.values()) {
            TICKER_CACHE.put(reference, new HashMap<>());
        }

        // Compute all possible Currency Pairs.
        for (Currency base : BASES) {
            for (Currency counter : COUNTERS) {
                if (base.equals(counter))
                    continue;

                CURRENCY_PAIRS.add(new CurrencyPair(base, counter));
            }
        }
    }

    private final ExchangeTickerCacheUpdater exchangeTickerCacheUpdater;
    private final LogService logService;

    @Autowired
    public TickerCacheService(ExchangeTickerCacheUpdater exchangeTickerCacheUpdater,
                              LogService logService) {
        this.exchangeTickerCacheUpdater = exchangeTickerCacheUpdater;
        this.logService = logService;
    }

    /**
     * Retrieves the cached Ticker for the given Exchange and Currency Pair.
     *
     * @param exchangeReference The ExchangeReference for the desired Exchange.
     * @param currencyPair      The CurrencyPair of the Ticker.
     * @return The Ticker, if one exists.
     */
    public Ticker getTicker(ExchangeReference exchangeReference, CurrencyPair currencyPair) {
        return TICKER_CACHE.get(exchangeReference).get(currencyPair);
    }

    /**
     * Returns all the CurrencyPairs that are currently cached for a given Exchange.
     *
     * @param exchangeReference The reference to the Exchange.
     * @return A Set containing all cached CurrencyPairs for the Exchange.
     */
    public Set<CurrencyPair> getCachedCurrencyPairsForExchange(ExchangeReference exchangeReference) {
        return TICKER_CACHE.get(exchangeReference).keySet();
    }

    /**
     * Loads and caches the prices of crypto currencies every 2 minutes.
     */
    @Async
    @Scheduled(initialDelay = 0, fixedDelay = 120_000)
    public void updateAllExchanges() {

        logService.logInfo(getClass(), "Updating Ticker Cache.");

        // For every Exchange, update the cache asynchronously.
        for (ExchangeReference reference : ExchangeReference.values()) {
            exchangeTickerCacheUpdater.updateExchangeCache(reference).thenAcceptAsync(exchangeCache -> {
                TICKER_CACHE.put(reference, exchangeCache);
                logService.logInfo(getClass(), "Cached " + exchangeCache.size() + " Tickers for " + reference.name());
            });
        }
    }

}
