package com.mitchtalmadge.apps.discord.professor_doge.command.listeners;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;

public interface CommandListener {

    /**
     * Called when a command is received.
     *
     * @param command The command that was received.
     * @return What the bot should say in reply. Null for no reply.
     */
    String onCommand(Command command);

}
