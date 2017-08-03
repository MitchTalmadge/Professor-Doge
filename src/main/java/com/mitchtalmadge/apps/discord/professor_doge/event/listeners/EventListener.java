package com.mitchtalmadge.apps.discord.professor_doge.event.listeners;

import net.dv8tion.jda.core.events.Event;

public interface EventListener<E extends Event> {

    void onEvent(E event);

}
