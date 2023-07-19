package jne.engine.events.types;

import jne.engine.screens.components.Component;
import jne.engine.utils.IComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class ScriptEvent {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Init extends Event {

        private final IComponent component;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Tooltip extends Event {

        private final IComponent component;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Update extends Event {

        private final IComponent component;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Press extends Event {

        private final IComponent component;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class FailPress extends Event {

        private final IComponent component;

    }

}
