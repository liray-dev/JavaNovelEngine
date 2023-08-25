package jne.engine.events.types;

import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class ScreenEvent {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class UpdateInfo extends Event {

        private final ComponentsListener screen;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Close extends Event {

        private final ComponentsListener screen;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Render extends Event {

        private final float partialTick;
        private final float width, height;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MouseMove extends Event {

        private final int mouseX, mouseY;


    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MouseInput extends Event {

        private final MouseClickType type;

        private final int mouseX, mouseY, button;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MouseClickMove extends Event {

        private final int mouseX, mouseY, button;

        private final long last;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Wheel extends Event {

        private final int mouseX, mouseY, value;

    }

    public static class Tick extends Event {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Keyboard extends Event {

        private final KeyboardType type;
        private final char character;
        private final int button;

    }

}
