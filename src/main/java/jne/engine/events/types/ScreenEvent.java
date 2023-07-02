package jne.engine.events.types;

import jne.engine.utils.KeyboardType;
import jne.engine.utils.MouseClickType;
import lombok.Data;

public class ScreenEvent {

    @Data
    public static class Render extends Event {

        private final float partialTick;

    }

    @Data
    public static class MouseMove extends Event {

        private final int mouseX, mouseY;


    }
    @Data
    public static class MouseInput extends Event {

        private final MouseClickType type;

        private final int mouseX, mouseY, button;

    }
    @Data
    public static class MouseClickMove extends Event {

        private final int mouseX, mouseY, button;

        private final long last;

    }

    public static class Tick extends Event {
    }

    @Data
    public static class Keyboard extends Event {

        private final KeyboardType type;
        private final char character;
        private final int button;

    }

}
