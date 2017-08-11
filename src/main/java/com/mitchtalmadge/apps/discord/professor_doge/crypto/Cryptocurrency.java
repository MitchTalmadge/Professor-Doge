package com.mitchtalmadge.apps.discord.professor_doge.crypto;

import org.knowm.xchange.currency.Currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a cryptocurrency, with its exchange and prices.
 */
public class Cryptocurrency implements Comparable<Cryptocurrency> {

    /**
     * The exchange that this cryptocurrency is from.
     */
    private ExchangeReference exchangeReference;

    /**
     * The base of this cryptocurrency.
     */
    private Currency base;

    /**
     * Counter currencies mapped to prices.
     */
    private Map<Currency, BigDecimal> prices = new TreeMap<>();

    /**
     * Constructs a Cryptocurrency instance.
     *
     * @param exchangeReference The exchange that this cryptocurrency is from.
     * @param base              The base of this cryptocurrency.
     */
    public Cryptocurrency(ExchangeReference exchangeReference, Currency base) {
        this.exchangeReference = exchangeReference;
        this.base = base;
    }

    /**
     * @return The exchange that this cryptocurrency is from.
     */
    public ExchangeReference getExchangeReference() {
        return exchangeReference;
    }

    /**
     * @return The base of this cryptocurrency.
     */
    public Currency getBase() {
        return base;
    }

    /**
     * @return The prices of the counters, relative to this cryptocurrency.
     */
    public Map<Currency, BigDecimal> getPrices() {
        return prices;
    }

    /**
     * Sets the price of a counter for this cryptocurrency.
     *
     * @param counter The counter.
     * @param price   The price of the counter.
     */
    public void setPrice(Currency counter, BigDecimal price) {
        this.prices.put(counter, price);
    }

    @Override
    public int compareTo(Cryptocurrency o) {
        return this.base.compareTo(o.base);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cryptocurrency that = (Cryptocurrency) o;

        if (exchangeReference != that.exchangeReference) return false;
        if (base != null ? !base.equals(that.base) : that.base != null) return false;
        return prices != null ? prices.equals(that.prices) : that.prices == null;
    }

    @Override
    public int hashCode() {
        int result = exchangeReference != null ? exchangeReference.hashCode() : 0;
        result = 31 * result + (base != null ? base.hashCode() : 0);
        result = 31 * result + (prices != null ? prices.hashCode() : 0);
        return result;
    }
}
