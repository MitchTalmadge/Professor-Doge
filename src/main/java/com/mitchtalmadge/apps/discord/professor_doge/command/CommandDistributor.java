package com.mitchtalmadge.apps.discord.professor_doge.command;

import com.mitchtalmadge.apps.discord.professor_doge.command.listeners.CommandListener;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class CommandDistributor {

    private static final CommandPattern EMPTY_COMMAND_PATTERN = new CommandPattern() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return CommandPattern.class;
        }

        @Override
        public String[] value() {
            return new String[0];
        }

        @Override
        public boolean strict() {
            return true;
        }
    };

    /**
     * All Command Distribution Listeners.
     */
    private static final Set<CommandListener> LISTENERS = new HashSet<>();

    static {
        // Find all Command Distributor Listeners.
        Set<Class<? extends CommandListener>> listeners =
                new Reflections(CommandDistributor.class.getPackage().getName()).getSubTypesOf(CommandListener.class);

        // Instantiate and insert each Listener into the LISTENERS set.
        listeners.forEach(c -> {
            try {
                CommandListener listener = c.newInstance();
                LISTENERS.add(listener);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Default constructor made private to prevent accidental instantiation.
     */
    private CommandDistributor() {
    }

    /**
     * Distributes the given command to the proper Command Listener.
     *
     * @param command The command to distribute.
     */
    public static void onCommand(Command command) {

        // Try to find the most specific command listener.
        CommandPatternComparator commandPatternComparator = new CommandPatternComparator(command);
        CommandListener mostSpecificListener = null;
        for (CommandListener listener : LISTENERS) {
            CommandPattern commandPattern = listener.getClass().getAnnotation(CommandPattern.class);
            if (commandPattern == null) {
                System.err.println("Command Distribution Listener '" + listener.getClass().getSimpleName() + "' is missing a Command Pattern.");
                return;
            }

            if (mostSpecificListener != null) {
                // Check if this listener is more specific than the already "most specific" listener.
                if (commandPatternComparator.compare(mostSpecificListener.getClass().getAnnotation(CommandPattern.class), commandPattern) > 0)
                    mostSpecificListener = listener;
            } else {
                // Check if this listener is more specific than an empty pattern.
                if (commandPatternComparator.compare(EMPTY_COMMAND_PATTERN, commandPattern) > 0)
                    mostSpecificListener = listener;
            }
        }

        // Send the command to the proper listener, or send an error message if no listener matched the command.
        if (mostSpecificListener == null) {
            command.getMessageReceivedEvent().getChannel().sendMessage("doge does not know what human wants?!").queue();
        } else {
            String response = mostSpecificListener.onCommand(command);
            if (response != null) {
                command.getMessageReceivedEvent().getChannel().sendMessage(response).queue();
            }
        }
    }

}
