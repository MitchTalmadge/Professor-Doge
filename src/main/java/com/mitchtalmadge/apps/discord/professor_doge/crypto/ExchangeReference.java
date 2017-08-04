package com.mitchtalmadge.apps.discord.professor_doge.crypto;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bittrex.v1.BittrexExchange;
import org.knowm.xchange.ccex.CCEXExchange;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.coinbase.CoinbaseExchange;
import org.knowm.xchange.gdax.GDAXExchange;
import org.knowm.xchange.gemini.v1.GeminiExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

public enum ExchangeReference {

    BITFINEX("Bitfinex", BitfinexExchange.class),
    BITSTAMP("Bitstamp", BitstampExchange.class),
    BITTREX("Bittrex", BittrexExchange.class),
    CCEX("CCEX", CCEXExchange.class),
    CEXIO("CexIO", CexIOExchange.class),
    COINBASE("Coinbase", CoinbaseExchange.class),
    GDAX("GDAX", GDAXExchange.class),
    GEMINI("Gemini", GeminiExchange.class),
    KRAKEN("Kraken", KrakenExchange.class),
    POLONIEX("Poloniex", PoloniexExchange.class);


    private final String exchangeName;
    private final Class<? extends BaseExchange> exchangeClass;
    private final MarketDataService marketDataService;

    ExchangeReference(String exchangeName, Class<? extends BaseExchange> exchangeClass) {
        this.exchangeName = exchangeName;
        this.exchangeClass = exchangeClass;
        this.marketDataService = ExchangeFactory.INSTANCE.createExchange(exchangeClass.getName()).getMarketDataService();
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
