package com.mitchtalmadge.apps.discord.professor_doge.crypto.cron;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GeneralTickerCacher {

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

    static {
        // Compute all possible Currency Pairs.
        for (Currency base : BASES) {
            for (Currency counter : COUNTERS) {
                if (base.equals(counter))
                    continue;

                CURRENCY_PAIRS.add(new CurrencyPair(base, counter));
            }
        }
    }

    private final SpecificTickerCacher specificTickerCacher;
    private final LogService logService;

    @Autowired
    public GeneralTickerCacher(@Lazy SpecificTickerCacher specificTickerCacher,
                               LogService logService) {
        this.specificTickerCacher = specificTickerCacher;
        this.logService = logService;
    }

    /**
     * Loads and caches the prices of crypto currencies every 5 minutes.
     */
    @Async
    @Scheduled(cron = "0 */5 * * * *")
    public void updateAllPrices() {

        logService.logInfo(getClass(), "Updating Prices.");

        // For every Exchange, update the prices asynchronously.
        for (ExchangeReference reference : ExchangeReference.values()) {
            specificTickerCacher.updatePricesForExchange(reference);
        }

    }

}
