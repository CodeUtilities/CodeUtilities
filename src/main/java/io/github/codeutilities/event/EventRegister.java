package io.github.codeutilities.event;

import io.github.codeutilities.event.listening.EventWatcher;
import io.github.codeutilities.event.listening.IEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegister {
    private static EventRegister instance;

    private Map<Class<? extends IEvent>, List<Method>> listeners = new HashMap<>();

    private EventRegister() {
        instance = this;
    }


    @SuppressWarnings("unchecked")
    public void registerListener(IEventListener listener) {
        for (Method m : listener.getClass().getMethods()) {
            if (m.isAnnotationPresent(EventWatcher.class)) {
                var params = m.getParameterTypes();

                if (params.length > 1)
                    throw new RuntimeException("Unable to parse listener with multiple parameters");

                if (!params[0].isAssignableFrom(IEvent.class))
                    throw new RuntimeException("Event watchers must have an event as its parameter");

               if (!this.listeners.containsKey(params[0])) {
                   this.listeners.put((Class<? extends IEvent>) params[0], new ArrayList<>());
               }
               var eventListeners = this.listeners.get(params[0]);

               eventListeners.add(m);
            }
        }
    }

    public void dispatch(IEvent event) {

        var eventListeners = this.listeners.get(event.getClass());
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
