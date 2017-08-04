package com.mitchtalmadge.apps.discord.professor_doge.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.cache.TickerCacheService;
import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

@Service
public class CryptocurrencyService {

    private final LogService logService;
    private final TickerCacheService tickerCacheService;

    @Autowired
    public CryptocurrencyService(LogService logService,
                                 TickerCacheService tickerCacheService) {
        this.logService = logService;
        this.tickerCacheService = tickerCacheService;
    }

    /**
     * Gets all the queryable CurrencyPairs for an Exchange.
     * <p>
     * CurrencyPairs will be sorted alphabetically, starting with the base, then the counter.
     *
     * @param exchangeReference The reference to the Exchange.
     * @return A Set containing the CurrencyPairs which can be queried for this Exchange.
     */
    public Set<CurrencyPair> getExchangeCurrencyPairs(ExchangeReference exchangeReference) {
        return new TreeSet<>(this.tickerCacheService.getCachedCurrencyPairsForExchange(exchangeReference));
    }

    /**
     * Determines the price of a CurrencyPair for an Exchange.
     *
     * @param exchangeReference The Exchange.
     * @param currencyPair      The CurrencyPair.
     * @return The price of the CurrencyPair on the Exchange, or null if the CurrencyPair is not on the Exchange.
     */
    public BigDecimal getPrice(ExchangeReference exchangeReference, CurrencyPair currencyPair) {
        Ticker ticker = this.tickerCacheService.getTicker(exchangeReference, currencyPair);
        return ticker != null ? ticker.getBid() : null;
    }

}
