package com.mitchtalmadge.apps.discord.professor_doge.command.listeners;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.command.CommandPattern;

@CommandPattern(value = {"help"}, strict = true)
public class HelpCommandListener implements CommandListener {

    @Override
    public String onCommand(Command command) {
        return "halp is on the way!!1!";
    }

}
