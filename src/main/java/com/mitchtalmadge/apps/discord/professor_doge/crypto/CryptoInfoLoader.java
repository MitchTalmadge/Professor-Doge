package com.mitchtalmadge.apps.discord.professor_doge.crypto;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.gdax.GDAXExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;

public class CryptoInfoLoader {

    private static final Exchange GDAX = ExchangeFactory.INSTANCE.createExchange(GDAXExchange.class.getName());
    private static final MarketDataService GDAX_MARKET = GDAX.getMarketDataService();

    public static String getGDAXPrice(CurrencyPair currencyPair) {
        try {
            Ticker ticker = GDAX_MARKET.getTicker(currencyPair);
            if (ticker != null)
                return ticker.getBid() + " " + currencyPair.counter.getCurrencyCode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }
        return null;
    }

}
