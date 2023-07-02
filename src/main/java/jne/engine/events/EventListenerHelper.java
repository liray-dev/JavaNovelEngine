package jne.engine.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventListenerHelper {

    private final static Map<Class<? extends Event>, Set<EventListener>> listeners = new HashMap<>();

    public static void register(Object object) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();

        ArrayList<EventListener> filteredMethods = new ArrayList<>();

        for (Method method : methods) {
            SubscribeEvent annotation = method.getAnnotation(SubscribeEvent.class);
            if (annotation != null) {
                filteredMethods.add(new EventListener(annotation.priority(), object, method));
            }
        }

        filteredMethods.forEach(it -> {

            Method method = it.getMethod();

            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) return;

            Class<?> parameterType = parameterTypes[0];


            Set<EventListener> eventListeners = listeners.computeIfAbsent((Class<? extends Event>) parameterType, func -> new TreeSet<EventListener>() {
            });

            eventListeners.add(it);
        });
    }

    public static void unregister(Object object) {
        for (Set<EventListener> eventListeners : listeners.values()) {
            eventListeners.removeIf(listener -> listener.getSubscriber().getClass() == object.getClass());
        }
    }

    public static void post(Event event) {
        Set<EventListener> eventListeners = listeners.get(event.getClass());

        if (eventListeners == null) return;

        eventListeners.forEach(it -> {
            if (event.isCanceled()) {
                return;
            }
            try {
                it.getMethod().invoke(it.getSubscriber(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
