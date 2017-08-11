package com.mitchtalmadge.apps.discord.professor_doge.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.service.LogService;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bittrex.v1.BittrexExchange;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.gdax.GDAXExchange;
import org.knowm.xchange.gemini.v1.GeminiExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public enum ExchangeReference {

    BITFINEX("Bitfinex", BitfinexExchange.class),
    BITSTAMP("Bitstamp", BitstampExchange.class),
    BITTREX("Bittrex", BittrexExchange.class),
    CEXIO("CEX.IO", CexIOExchange.class),
    GDAX("GDAX", GDAXExchange.class),
    GEMINI("Gemini", GeminiExchange.class),
    KRAKEN("Kraken", KrakenExchange.class),
    POLONIEX("Poloniex", PoloniexExchange.class);

    private final String exchangeName;
    private final Class<? extends BaseExchange> exchangeClass;
    private MarketDataService marketDataService;

    ExchangeReference(String exchangeName, Class<? extends BaseExchange> exchangeClass) {
        this.exchangeName = exchangeName;
        this.exchangeClass = exchangeClass;
    }

    /**
     * Loads the Exchange Market Data Services.
     * This is an inner class so that it can be initialized as a Spring Component.
     */
    @Component
    public static class ExchangeReferenceLoader {

        private final LogService logService;

        @Autowired
        public ExchangeReferenceLoader(LogService logService) {
            this.logService = logService;
        }

        @PostConstruct
        private void init() {
            for (ExchangeReference exchangeReference : ExchangeReference.values()) {
                try {
                    exchangeReference.marketDataService = ExchangeFactory.INSTANCE.createExchange(exchangeReference.exchangeClass.getName()).getMarketDataService();
                } catch (Exception e) {
                    logService.logException(getClass(), e, "Could not load Market Data Service for Exchange");
                    exchangeReference.marketDataService = null;
                }
            }
        }
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public Class<? extends BaseExchange> getExchangeClass() {
        return exchangeClass;
    }

    public MarketDataService getMarketDataService() {
        return this.marketDataService;
    }

    /**
     * Finds an ExchangeReference with the given Exchange name.
     *
     * @param exchangeName The name of the Exchange.
     * @return The matching ExchangeReference, or null if none matched.
     */
    public static ExchangeReference getByExchangeName(String exchangeName) {
        for (ExchangeReference exchangeReference : values())
            if (exchangeReference.exchangeName.equalsIgnoreCase(exchangeName))
                return exchangeReference;

        return null;
    }

}
