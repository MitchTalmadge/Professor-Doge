package com.mitchtalmadge.apps.discord.professor_doge.util;

import java.util.Random;

public class MessageUtils {

    /**
     * From the possible messages provided, returns a random message.
     *
     * @param messages The possible messages.
     * @return A random message from those provided.
     */
    public static String variationOf(String... messages) {
        return messages[new Random().nextInt(messages.length)];
    }

    /**
     * Pads a message to a specific length, using the given character as padding.
     *
     * @param message The message to pad.
     * @param padding The character to use as padding.
     * @param length  The length of the padded message.
     * @return The padded message.
     */
    public static String padToLength(String message, char padding, int length) {
        StringBuilder paddedMessage = new StringBuilder(message);

        // Start at the end of the message and work up until we've reached length.
        for (int i = message.length(); i <= length; i++)
            paddedMessage.append(padding);

        return paddedMessage.toString();
    }

}
