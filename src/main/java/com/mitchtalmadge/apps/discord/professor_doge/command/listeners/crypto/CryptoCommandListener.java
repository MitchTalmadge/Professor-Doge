package com.mitchtalmadge.apps.discord.professor_doge.command.listeners.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.command.CommandPattern;
import com.mitchtalmadge.apps.discord.professor_doge.command.listeners.CommandListener;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.CryptocurrencyService;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.util.MessageUtils;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@CommandPattern({"c"})
public class CryptoCommandListener extends CommandListener {

    private final CryptocurrencyService cryptocurrencyService;

    @Autowired
    public CryptoCommandListener(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }

    @Override
    public String onCommand(Command command) {
        // Extra arguments are passed in.
        if (command.getArgs().length > 1) {

            // Try to find a matching ExchangeReference
            ExchangeReference exchangeReference = ExchangeReference.getByExchangeName(command.getArgs()[1]);
            if (exchangeReference != null) {
                return "So much money:" +
                        "```http\n" +
                        summarizeExchange(exchangeReference) +
                        "```\n";
            }

            // No match.
            return "You confused Doge. What is `" + String.join(" ", command.getArgs()) + "`?!\n\n" +
                    "Doge suggests `help c`.";
        }

        // Default response when no extra arguments are passed in.
        return "Here is a good wisdom:" +
                "```http\n" +
                summarizeExchange(ExchangeReference.GDAX) +
                "```\n" +
                "Try `help c` for more wisdom!!";
    }

    /**
     * Summarizes all the CurrencyPairs for an Exchange.
     *
     * @param exchangeReference The Exchange to summarize.
     * @return The summary, starting with the name of the Exchange,
     * then following with multiple lines in the format: "LTC: 24 USD / 20 EUR / 0.006 BTC\n"
     */
    private String summarizeExchange(ExchangeReference exchangeReference) {
        // All currency pairs for the exchange.
        Set<CurrencyPair> currencyPairs = cryptocurrencyService.getExchangeCurrencyPairs(exchangeReference);

        // Maps unique bases to sets of unique counters.
        Map<Currency, Set<Currency>> currencyMap = new TreeMap<>();

        // Add unique bases and counters to the map
        for (CurrencyPair currencyPair : currencyPairs) {
            if (!currencyMap.containsKey(currencyPair.base))
                currencyMap.put(currencyPair.base, new TreeSet<>());

            currencyMap.get(currencyPair.base).add(currencyPair.counter);
        }

        // Stores the summary.
        StringBuilder summary = new StringBuilder();

        summary.append(exchangeReference.getExchangeName()).append("\n")
                .append("------------------------------------------").append("\n");

        // Build the summary.
        for (Currency base : currencyMap.keySet()) {
            // ex. "LTC: "
            String prefix = base.getCurrencyCode() + ":";
            summary.append(MessageUtils.padToLength(prefix, ' ', 5));

            // ex. "24 USD | 20 EUR | 0.006 BTC | "
            for (Currency counter : currencyMap.get(base)) {
                BigDecimal price = cryptocurrencyService.getPrice(exchangeReference, new CurrencyPair(base, counter));
                price = price.round(new MathContext(6, RoundingMode.HALF_UP));

                summary
                        .append(MessageUtils.padToLength(price.toPlainString(), ' ', 7))
                        .append(counter.getCurrencyCode())
                        .append(" | ");
            }

            // Remove last " | " and add newline
            summary.delete(summary.length() - 3, summary.length()).append("\n");
        }

        return summary.toString();
    }


}
