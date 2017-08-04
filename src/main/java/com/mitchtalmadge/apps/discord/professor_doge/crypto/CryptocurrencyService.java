package com.mitchtalmadge.apps.discord.professor_doge.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.cron.GeneralTickerCacher;
import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CryptocurrencyService {

    /**
     * The cache containing a reference to each Exchange, their currency pairs, and their respective tickets.
     */
    private static final Map<ExchangeReference, Map<CurrencyPair, Ticker>> TICKER_CACHE = new ConcurrentHashMap<>();

    static {
        // Initialize the cache.
        for (ExchangeReference reference : ExchangeReference.values()) {
            TICKER_CACHE.put(reference, new HashMap<>());
        }
    }

    private final LogService logService;
    private final GeneralTickerCacher generalTickerCacher;

    @Autowired
    public CryptocurrencyService(LogService logService,
                                 GeneralTickerCacher generalTickerCacher) {
        this.logService = logService;
        this.generalTickerCacher = generalTickerCacher;
    }

    @PostConstruct
    private void init() {
        this.generalTickerCacher.updateAllPrices();
    }

    /**
     * Updates the cache of the Exchange in the Ticker Cache.
     *
     * @param reference     The reference to the Exchange whose cache is being updated.
     * @param exchangeCache The cache of the Exchange.
     */
    public void updateExchangeCache(ExchangeReference reference, Map<CurrencyPair, Ticker> exchangeCache) {
        TICKER_CACHE.put(reference, exchangeCache);
        logService.logInfo(getClass(), "Cached " + exchangeCache.size() + " Tickers for " + reference.name());
    }

    /**
     * Retrieves the cached Ticker for the given Exchange and Currency Pair.
     *
     * @param exchangeReference The ExchangeReference for the desired Exchange.
     * @param currencyPair      The CurrencyPair of the Ticker.
     * @return The Ticker, if one exists.
     */
    public static Ticker getTicker(ExchangeReference exchangeReference, CurrencyPair currencyPair) {
        return TICKER_CACHE.get(exchangeReference).get(currencyPair);
    }

    public static String getGDAXPrice(CurrencyPair currencyPair) {
        Ticker ticker = getTicker(ExchangeReference.GDAX, currencyPair);

        if (ticker != null)
            return ticker.getBid() + " " + currencyPair.counter.getCurrencyCode();

        return null;
    }

}
