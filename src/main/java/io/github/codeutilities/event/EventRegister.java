package io.github.codeutilities.event;

import io.github.codeutilities.event.listening.EventWatcher;
import io.github.codeutilities.event.listening.IEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegister {
    private static EventRegister instance;

    private Map<String, List<Method>> listeners = new HashMap<>();

    private EventRegister() {
        instance = this;
    }

    public void registerListener(IEventListener listener) {
        for (Method m : listener.getClass().getMethods()) {
            if (m.isAnnotationPresent(EventWatcher.class)) {
                var params = m.getParameterTypes();

                if (params.length > 1)
                    throw new RuntimeException("Unable to parse listener with multiple parameters");

                if (!params[0].isAssignableFrom(IEvent.class))
                    throw new RuntimeException("Event watchers must have an event as its parameter");

               var eventListeners = this.listeners.get(params[0].getSimpleName());

               eventListeners.add(m);
               this.listeners.put(params[0].getSimpleName(), eventListeners);
            }
        }
    }

    public void dispatch(IEvent event) {
        String name = event.getClass().getSimpleName();

        var eventListeners = this.listeners.get(name);
        if (eventListeners.isEmpty())
            return;

        for (Method m : eventListeners) {
            try {
                m.invoke(event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static EventRegister getInstance() {
        return instance == null ? new EventRegister() : instance;
    }

}
