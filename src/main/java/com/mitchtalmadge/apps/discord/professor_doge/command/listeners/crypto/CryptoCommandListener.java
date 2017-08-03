package com.mitchtalmadge.apps.discord.professor_doge.command.listeners.crypto;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.command.CommandPattern;
import com.mitchtalmadge.apps.discord.professor_doge.command.listeners.CommandListener;

@CommandPattern({"crypto"})
public class CryptoCommandListener implements CommandListener {

    @Override
    public String onCommand(Command command) {
        if (command.getArgs().length > 1) {
            return "doge is confuse, what is `" + String.join(" ", command.getArgs()) + "`?!\n" +
                    "here is doge wisdom dictionary:\n" +
                    "```http\n" +
                    "crypto: General Overview\n" +
                    "crypto [gdax|bitnifex|poloniex]: Exchange Overview\n" +
                    "```";
        }

        return "here is a good wisdom:" +
                "```http\n" +
                "LTC: $42.6114\t€35.9759101806\t0.0156615 BTC\t(-0.2%)\n" +
                "ETH: $200.12\t€180.23\t0.12 BTC\t(+0.3%)\n" +
                "```";
    }

}
