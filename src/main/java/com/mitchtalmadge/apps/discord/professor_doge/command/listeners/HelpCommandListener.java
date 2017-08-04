package com.mitchtalmadge.apps.discord.professor_doge.command.listeners;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.command.CommandPattern;
import com.mitchtalmadge.apps.discord.professor_doge.util.MessageUtils;

@CommandPattern({"help"})
public class HelpCommandListener extends CommandListener {

    @Override
    public String onCommand(Command command) {

        String helpContent = null;

        if (command.getArgs().length > 1) {
            switch (command.getArgs()[1]) {
                case "c":
                    helpContent = "Cryptocurrency: General Overview\n" +
                            "c [gdax|bitnifex|poloniex]: Exchange Overview";
            }
        }

        String message = MessageUtils.variationOf(
                "Pls refer to Doge's wisdom dictionary:",
                "Consult with the wisdom of Doge:",
                "Doge knows all:",
                "Wow, much help:",
                "Such wisdom:"
        ) + "\n" +
                "```http\n";

        if (helpContent != null)
            message += helpContent + "\n";
        else
            message += "c: Cryptocurrency Info\n";

        message += "```";

        return message;
    }

}
