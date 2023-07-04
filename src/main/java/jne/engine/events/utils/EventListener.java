package jne.engine.events.utils;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;

@Data
@RequiredArgsConstructor
public class EventListener implements Comparable<EventListener> {

    private final Class<?>[] exclusion;
    private final EventPriority priority;
    private final Object subscriber;
    private final Method method;

    @Override
    public int compareTo(EventListener o) {
        boolean flag = Arrays.stream(exclusion)
                .anyMatch(excludedClass -> excludedClass == o.subscriber.getClass());

        if (flag) {
            return -1;
        }

        int value = o.priority.ordinal() - this.priority.ordinal();

        if (exclusion.length > 0 && o.exclusion.length == 0 && value == 0) {
            return -1;
        }

        return value;
    }

}
