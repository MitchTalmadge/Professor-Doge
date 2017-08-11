package com.mitchtalmadge.apps.discord.professor_doge.command.listeners.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.Cryptocurrency;
import org.knowm.xchange.currency.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CryptoFormatter {

    /**
     * How many significant digits for the prices.
     */
    private static final int SIGNIFICANT_DIGITS = 4;

    /**
     * From the provided cryptocurrencies, maps the counter currencies to the required padding width for equal width
     * display when in columns.
     *
     * @param cryptocurrencies The cryptocurrencies.
     * @return A map of the counter currencies and the paddings that should be applied.
     */
    public static Map<Currency, Integer> calculatePaddingWidths(Set<Cryptocurrency> cryptocurrencies) {
        Map<Currency, Integer> paddings = new HashMap<>();

        // For each cryptocurrency
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {

            // Format and measure each counter price
            for (Map.Entry<Currency, BigDecimal> priceEntry : cryptocurrency.getPrices().entrySet()) {
                Integer currentPadding = paddings.get(priceEntry.getKey());

                // Format the price
                BigDecimal formattedPrice = formatPrice(priceEntry.getValue());
                int priceLength = formattedPrice.toPlainString().length();

                // If the formatted price's length is greater than the current longest, update it.
                if (currentPadding == null || priceLength > currentPadding)
                    paddings.put(priceEntry.getKey(), priceLength);
            }
        }

        return paddings;
    }

    /**
     * Formats the price to a standard number of significant digits.
     *
     * @param price The price.
     * @return The formatted price.
     */
    public static BigDecimal formatPrice(BigDecimal price) {
        return price.setScale(SIGNIFICANT_DIGITS, BigDecimal.ROUND_HALF_UP);
    }

}
