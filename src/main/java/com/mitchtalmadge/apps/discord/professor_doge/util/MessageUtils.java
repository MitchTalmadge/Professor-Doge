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

}
