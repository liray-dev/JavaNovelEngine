package jne.engine.events.test;

import jne.engine.events.Event;
import jne.engine.events.SubscribeEvent;

public class TestEvents {

    @SubscribeEvent
    public void test(Event event) {
        System.out.println(event.getClass().getName());
        event.value += 1;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void test2(CoolEvent event) {
        System.out.println(event.getClass().getName());
        if (true) {
            event.setCanceled(true);
            return;
        }
        event.value += 10;
    }

}
