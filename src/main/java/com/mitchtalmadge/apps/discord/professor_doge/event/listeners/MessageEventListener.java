package com.mitchtalmadge.apps.discord.professor_doge.event.listeners;

import com.mitchtalmadge.apps.discord.professor_doge.command.Command;
import com.mitchtalmadge.apps.discord.professor_doge.command.CommandDistributor;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MessageEventListener implements EventListener<MessageReceivedEvent> {

    public static final String COMMAND_PREFIX = ".pd";

    @Override
    public void onEvent(MessageReceivedEvent event) {
        // Ignore messages from ourself.
        if (event.getAuthor().equals(event.getJDA().getSelfUser()))
            return;

        switch (event.getChannelType()) {
            case TEXT:
                // Check for Command Prefix
                if (event.getMessage().getRawContent().startsWith(COMMAND_PREFIX + " ")) {
                    // Split the message into arguments
                    String[] args = event.getMessage().getRawContent().substring(COMMAND_PREFIX.length() + 1).split("\\s");

                    // Create a command instance
                    Command command = new Command(event, args);
                    CommandDistributor.onCommand(command);
                }
        }
    }

}
