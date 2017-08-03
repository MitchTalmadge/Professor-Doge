package com.mitchtalmadge.apps.discord.professor_doge.event;

import com.mitchtalmadge.apps.discord.professor_doge.event.listeners.EventListener;
import net.dv8tion.jda.core.events.Event;
import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventDistributor implements net.dv8tion.jda.core.hooks.EventListener {

    /**
     * Maps Event Distribution Listeners to their generic Event types.
     */
    private static final Map<Class<? extends Event>, EventListener> LISTENER_MAP = new HashMap<>();

    static {
        // Find all listeners.
        Set<Class<? extends EventListener>> listeners =
                new Reflections(EventDistributor.class.getPackage().getName()).getSubTypesOf(EventListener.class);

        // Instantiate and insert each Listener into the LISTENER_MAP.
        listeners.forEach(c -> {
            try {
                EventListener eventListener = c.newInstance();
                //noinspection unchecked
                LISTENER_MAP.put(
                        (Class<? extends Event>) ((ParameterizedType) c.getGenericInterfaces()[0]).getActualTypeArguments()[0],
                        eventListener
                );
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onEvent(Event event) {

        // Look for a listener for the event.
        if (LISTENER_MAP.containsKey(event.getClass())) {
            //noinspection unchecked
            LISTENER_MAP.get(event.getClass()).onEvent(event);
        }

    }

}
