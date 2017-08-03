package com.mitchtalmadge.apps.discord.professor_doge.command.listeners.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.command.CommandPattern;
import com.mitchtalmadge.apps.discord.professor_doge.command.listeners.CommandListener;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.CryptoInfoLoader;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

@CommandPattern({"crypto"})
public class CryptoCommandListener implements CommandListener {

    @Override
    public String onCommand(Command command) {
        if (command.getArgs().length > 1) {
            return "You confused Doge. What is `" + String.join(" ", command.getArgs()) + "`?!\n";

        }

        return "Here is a good wisdom:" +
                "```http\n" +
                "LTC: " + summarizeCrypto(Currency.LTC) + "\n" +
                "ETH: " + summarizeCrypto(Currency.ETH) + "\n" +
                "BTC: " + summarizeCrypto(Currency.BTC) + "\n" +
                "```\n" +
                "Try also `help crypto` for more!1";
    }

    private String summarizeCrypto(Currency cryptoCurrency) {
        StringBuilder summary = new StringBuilder();

        summary.append(CryptoInfoLoader.getGDAXPrice(new CurrencyPair(cryptoCurrency, Currency.USD)));
        summary.append(" / ");
        summary.append(CryptoInfoLoader.getGDAXPrice(new CurrencyPair(cryptoCurrency, Currency.EUR)));
        summary.append(" / ");
        summary.append(CryptoInfoLoader.getGDAXPrice(new CurrencyPair(cryptoCurrency, Currency.BTC)));

        return summary.toString();
    }

}
