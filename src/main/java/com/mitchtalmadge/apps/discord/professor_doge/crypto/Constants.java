package com.mitchtalmadge.apps.discord.professor_doge.crypto;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.HashSet;
import java.util.Set;

public class Constants {

    /**
     * The base currencies that should be cached automatically.
     */
    public static final Currency[] CACHE_BASES = new Currency[]{
            new Currency("BCH"),
            new Currency("BCC"),
            new Currency("BCU"),
            new Currency("BTC"),
            new Currency("DASH"),
            new Currency("DOGE"),
            new Currency("EOS"),
            new Currency("ETH"),
            new Currency("GAS"),
            new Currency("IOTA"),
            new Currency("LTC"),
            new Currency("NEO"),
            new Currency("RRT"),
            new Currency("SAN"),
            new Currency("XMR"),
            new Currency("XRP"),
            new Currency("ZEC"),
    };

    /**
     * The counter currencies that should be cached automatically.
     */
    public static final Currency[] CACHE_COUNTERS = new Currency[]{
            new Currency("BTC"),
            new Currency("EUR"),
            new Currency("GBP"),
            new Currency("USD"),
            new Currency("USDT"),
    };

    /**
     * The pairs of base and counter currencies that should be cached automatically.
     */
    public static final Set<CurrencyPair> CACHE_CURRENCY_PAIRS = new HashSet<>();

    static {
        // Compute all cache currency pairs.
        for (Currency base : CACHE_BASES) {
            for (Currency counter : CACHE_COUNTERS) {
                if (base.equals(counter))
                    continue;

                CACHE_CURRENCY_PAIRS.add(new CurrencyPair(base, counter));
            }
        }
    }

}
