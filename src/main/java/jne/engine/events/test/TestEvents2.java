package jne.engine.events.test;

import jne.engine.events.Event;
import jne.engine.events.EventPriority;
import jne.engine.events.SubscribeEvent;

public class TestEvents2 {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void test(Event event) {
        System.out.println(event.getClass().getName() + " 2");
        event.value += 2;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void test2(CoolEvent event) {
        System.out.println(event.getClass().getName()+ " 2");
        event.value += 10;
    }

}
