package com.mitchtalmadge.apps.discord.professor_doge.command.listeners.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.command.CommandPattern;
import com.mitchtalmadge.apps.discord.professor_doge.command.listeners.CommandListener;
import com.mitchtalmadge.apps.discord.professor_doge.crypto.ExchangeReference;
import com.mitchtalmadge.apps.discord.professor_doge.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

@CommandPattern({"c"})
public class CryptoCommandListener extends CommandListener {

    private final ExchangeSummarizer exchangeSummarizer;

    @Autowired
    public CryptoCommandListener(ExchangeSummarizer exchangeSummarizer) {
        this.exchangeSummarizer = exchangeSummarizer;
    }

    @Override
    public String onCommand(Command command) {
        // Extra arguments are passed in.
        if (command.getArgs().length > 1) {

            // Try to find a matching ExchangeReference
            ExchangeReference exchangeReference = ExchangeReference.getByExchangeName(command.getArgs()[1]);
            if (exchangeReference != null) {
                String summary = exchangeSummarizer.summarizeExchange(exchangeReference);
                if (summary == null) {
                    return "Doge has no info about " + exchangeReference.getExchangeName() + " right now. :disappointed_relieved: such sorry... try again later?!";
                }
                return MessageUtils.variationOf("Wow, such money :money_with_wings: :money_with_wings:", "Wow", "Very gains :chart_with_upwards_trend:", "Many lambo :red_car:", "Doge found it!!1", "Doge try doge best :100:") +
                        "```http\n" +
                        summary +
                        "```\n";
            }

            // No match.
            return "You confused Doge. What is `" + String.join(" ", command.getArgs()) + "`?!\n\n" +
                    "Doge suggests `help c`.";
        }

        // Default response when no extra arguments are passed in.
        return MessageUtils.variationOf("Here is a good wisdom:", "Arise Chickun!!1", "Such Coinbase:", "Doge's favorite coins:") +
                "```http\n" +
                exchangeSummarizer.summarizeExchange(ExchangeReference.GDAX) +
                "```\n" +
                "Try `help c` for more wisdom!!";
    }


}
