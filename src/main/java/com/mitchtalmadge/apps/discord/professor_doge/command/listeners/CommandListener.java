package com.mitchtalmadge.apps.discord.professor_doge.command.listeners;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.util.InheritedComponent;

@InheritedComponent
public abstract class CommandListener {

    /**
     * Called when a command is received.
     *
     * @param command The command that was received.
     * @return What the bot should say in reply. Null for no reply.
     */
    public abstract String onCommand(Command command);

}
