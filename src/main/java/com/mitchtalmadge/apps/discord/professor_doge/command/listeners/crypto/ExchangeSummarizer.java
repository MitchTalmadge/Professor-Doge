package com.mitchtalmadge.apps.discord.professor_doge.command.listeners.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.crypto.Cryptocurrency;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.cache.CacheService;
import com.mitchtalmadge.apps.discord.professor_doge.util.MessageUtils;
import org.knowm.xchange.currency.Currency;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Summarizes Crypto Exchange Information
 */
@Component
public class ExchangeSummarizer {

    private final CacheService cryptoCacheService;

    @Autowired
    public ExchangeSummarizer(CacheService cryptoCacheService) {
        this.cryptoCacheService = cryptoCacheService;
    }

    /**
     * Summarizes all the CurrencyPairs for an Exchange.
     *
     * @param exchangeReference The Exchange to summarize.
     * @return The summary, starting with the name of the Exchange,
     * then following with multiple lines in the format: "LTC: 24 USD / 20 EUR / 0.006 BTC\n"
     */
    public String summarizeExchange(ExchangeReference exchangeReference) {
        // Map the currency bases to counters.
        Set<Cryptocurrency> cryptocurrencies = cryptoCacheService.getCachedCryptocurrenciesForExchange(exchangeReference);

        // No pairs means we have no data for this exchange.
        if (cryptocurrencies.isEmpty())
            return null;

        // Stores the summary.
        StringBuilder summary = new StringBuilder();

        // Append the Header
        appendHeader(exchangeReference, summary);

        // Calculate how wide each column should be.
        Map<Currency, Integer> paddingWidths = CryptoFormatter.calculatePaddingWidths(cryptocurrencies);

        // Build the summary.
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            // ex. "LTC: "
            String prefix = cryptocurrency.getBase().getCurrencyCode() + ":";
            summary.append(MessageUtils.padToLength(prefix, ' ', 5));

            // ex. "24 USD | 20 EUR | 0.006 BTC | "
            for (Map.Entry<Currency, BigDecimal> priceEntry : cryptocurrency.getPrices().entrySet()) {
                BigDecimal price = CryptoFormatter.formatPrice(priceEntry.getValue());

                summary
                        .append(MessageUtils.padToLength(price.toPlainString(), ' ', paddingWidths.get(priceEntry.getKey())))
                        .append(' ')
                        .append(MessageUtils.padToLength(priceEntry.getKey().getCurrencyCode(), ' ', 5))
                        .append(" | ");
            }

            // Remove last " | " and add newline
            summary.delete(summary.length() - 3, summary.length()).append("\n");
        }

        return summary.toString();
    }

    /**
     * Appends the header to the summary.
     *
     * @param exchangeReference The exchange.
     * @param summary           The summary to append to.
     */
    private void appendHeader(ExchangeReference exchangeReference, StringBuilder summary) {
        // Figure out when the cache was last updated.
        PrettyTime prettyTime = new PrettyTime();
        LocalDateTime lastUpdate = cryptoCacheService.getLastUpdate(exchangeReference);
        String lastUpdatedMessage = lastUpdate == null ? "(Not yet fetched)" : "(Updated " + prettyTime.format(Date.from(lastUpdate.atZone(ZoneId.systemDefault()).toInstant())) + ")";

        // Append the exchange name, last update time, and a horizontal rule.
        summary.append(exchangeReference.getExchangeName()).append(' ').append(lastUpdatedMessage).append("\n")
                .append("------------------------------------------").append("\n");
    }

}
