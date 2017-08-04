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

    BITFINEX(BitfinexExchange.class),
    BITSTAMP(BitstampExchange.class),
    BITTREX(BittrexExchange.class),
    CCEX(CCEXExchange.class),
    CEXIO(CexIOExchange.class),
    COINBASE(CoinbaseExchange.class),
    GDAX(GDAXExchange.class),
    GEMINI(GeminiExchange.class),
    KRAKEN(KrakenExchange.class),
    POLONIEX(PoloniexExchange.class);


    private final Class<? extends BaseExchange> exchangeClass;
    private final MarketDataService marketDataService;

    ExchangeReference(Class<? extends BaseExchange> exchangeClass) {
        this.exchangeClass = exchangeClass;
        this.marketDataService = ExchangeFactory.INSTANCE.createExchange(exchangeClass.getName()).getMarketDataService();
    }

    public Class<? extends BaseExchange> getExchangeClass() {
        return exchangeClass;
    }

    public MarketDataService getMarketDataService() {
        return this.marketDataService;
    }

}
