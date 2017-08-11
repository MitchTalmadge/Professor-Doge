package com.mitchtalmadge.apps.discord.professor_doge.crypto.cache;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.Cryptocurrency;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private static final Map<ExchangeReference, Map<CurrencyPair, Ticker>> TICKER_CACHE = new ConcurrentHashMap<>();
    private static final Map<ExchangeReference, TreeSet<Cryptocurrency>> CRYPTOCURRENCY_CACHE = new ConcurrentHashMap<>();
    private static final Map<ExchangeReference, LocalDateTime> CACHE_UPDATE_TIMES = new ConcurrentHashMap<>();

    static {
        // Initialize the cache.
        for (ExchangeReference reference : ExchangeReference.values()) {
            TICKER_CACHE.put(reference, new HashMap<>());
            CRYPTOCURRENCY_CACHE.put(reference, new TreeSet<>());
        }
    }

    private final TickerCacheService tickerCacheService;
    private final LogService logService;

    @Autowired
    public CacheService(TickerCacheService tickerCacheService,
                        LogService logService) {
        this.tickerCacheService = tickerCacheService;
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
     * Gets the cached cryptocurrencies for a particular exchange.
     *
     * @param exchangeReference The exchange.
     * @return The cached cryptocurrencies for the given exchange.
     */
    public Set<Cryptocurrency> getCachedCryptocurrenciesForExchange(ExchangeReference exchangeReference) {
        return CRYPTOCURRENCY_CACHE.get(exchangeReference);
    }

    /**
     * Determines when the exchange was last cached.
     *
     * @param exchangeReference The exchange.
     * @return The last time the exchange was cached.
     */
    public LocalDateTime getLastUpdate(ExchangeReference exchangeReference) {
        return CACHE_UPDATE_TIMES.get(exchangeReference);
    }

    /**
     * Loads and caches the prices of crypto currencies every 3 minutes.
     */
    @Async
    @Scheduled(initialDelay = 0, fixedDelay = 180_000)
    public void updateAllExchanges() {

        logService.logInfo(getClass(), "Updating Crypto Caches...");

        // For every Exchange, update the cache asynchronously.
        for (ExchangeReference reference : ExchangeReference.values()) {
            tickerCacheService.updateCache(reference).thenAcceptAsync(tickerCache -> {
                // Update the ticker cache.
                TICKER_CACHE.put(reference, tickerCache);

                // Update the cryptocurrency cache
                TreeSet<Cryptocurrency> cryptocurrencies = createCryptocurrenciesFromTickers(reference, tickerCache);
                CRYPTOCURRENCY_CACHE.put(reference, cryptocurrencies);

                // Record the time that the cache was updated for this exchange.
                CACHE_UPDATE_TIMES.put(reference, LocalDateTime.now());
                logService.logInfo(getClass(), "Cached " + tickerCache.size() + " Tickers and " + cryptocurrencies.size() + " Cryptocurrencies for " + reference.getExchangeName());
            }).exceptionally(e -> {
                logService.logException(getClass(), e, "Could not update cache for " + reference.getExchangeName());
                return null;
            });
        }
    }

    /**
     * From the ticker cache segment provided, creates a set of cryptocurrency instances.
     *
     * @param exchangeReference The exchange.
     * @param tickerCache       The ticker cache.
     * @return A sorted TreeSet containing new Cryptocurrency instances from the currencies in the ticker cache.
     */
    private TreeSet<Cryptocurrency> createCryptocurrenciesFromTickers(ExchangeReference exchangeReference, Map<CurrencyPair, Ticker> tickerCache) {
        // Maps the cryptocurrency's base to its instance.
        Map<Currency, Cryptocurrency> cryptocurrencies = new HashMap<>();

        // For each pair, add to (or create) the appropriate cryptocurrency instance.
        for (CurrencyPair pair : tickerCache.keySet()) {

            // Get existing
            Cryptocurrency cryptocurrency = cryptocurrencies.get(pair.base);

            // Create new if does not exist
            if (cryptocurrency == null)
                cryptocurrencies.put(pair.base, (cryptocurrency = new Cryptocurrency(exchangeReference, pair.base)));

            // Get and add price
            cryptocurrency.setPrice(pair.counter, tickerCache.get(pair).getLast());
        }

        // Convert values to tree set.
        return new TreeSet<>(cryptocurrencies.values());
    }


}
