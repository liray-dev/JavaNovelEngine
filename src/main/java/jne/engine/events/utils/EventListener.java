package jne.engine.events.utils;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Data
@RequiredArgsConstructor
public class EventListener implements Comparable<EventListener> {

    private final Class<?>[] exclusion;
    private final EventPriority priority;
    private final Object subscriber;
    private final Method method;

    @Override
    public int compareTo(EventListener o) {
        return o.priority.ordinal() - this.priority.ordinal();
    }

}
