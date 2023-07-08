package jne.engine.events;

import jne.engine.errors.ErrorManager;
import jne.engine.events.types.Event;
import jne.engine.events.utils.EventListener;
import jne.engine.events.utils.SubscribeEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventListenerHelper {

    private final static Map<Class<? extends Event>, TreeSet<EventListener>> listeners = new HashMap<>();

    public static void register(Object object) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();

        ArrayList<EventListener> filteredMethods = new ArrayList<>();

        for (Method method : methods) {
            SubscribeEvent annotation = method.getAnnotation(SubscribeEvent.class);
            if (annotation != null) {
                filteredMethods.add(new EventListener(annotation.exclusion(), annotation.priority(), object, method));
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
            eventListeners.removeIf(listener -> listener.getSubscriber() == object);
        }
    }

    public static void post(Event event) {
        if (!listeners.containsKey(event.getClass())) return;
        TreeSet<EventListener> eventListeners = new TreeSet<>(listeners.get(event.getClass()));


        HashSet<Class<?>> exclusion = new HashSet<>();

        eventListeners.forEach(it -> {
            if (event.isCanceled()) {
                return;
            }
            try {
                if (!exclusion.contains(it.getSubscriber().getClass())) {
                    exclusion.addAll(Arrays.asList(it.getExclusion()));
                    try {
                        it.getMethod().invoke(it.getSubscriber(), event);
                    } catch (Exception e) {
                        throw new IllegalAccessException("Errors when calling an event " + it.getSubscriber().getClass().getName() + " with event " + event.getClass().getName());
                    }
                }
            } catch (IllegalAccessException e) {
                ErrorManager.error(e);
            }
        });

    }

}
