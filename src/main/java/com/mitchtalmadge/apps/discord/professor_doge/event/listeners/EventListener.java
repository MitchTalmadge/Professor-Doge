package com.mitchtalmadge.apps.discord.professor_doge.event.listeners;

import com.mitchtalmadge.apps.discord.professor_doge.util.InheritedComponent;
import net.dv8tion.jda.core.events.Event;

@InheritedComponent
public abstract class EventListener<E extends Event> {

    public abstract void onEvent(E event);

}
