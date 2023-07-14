package jne.sceneeditor.utils;

import jne.engine.constants.Direction;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;
import jne.engine.screens.widgets.Label;
import jne.engine.utils.IWrapper;
import lombok.Data;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Data
public class MovableComponent implements IWrapper {

    private final Component component;
    private EditingTypes type = EditingTypes.NONE;
    private final Color toolColor = new Color(0x383838);
    private final Color barColor = new Color(0x181818);
    private boolean tooltip = false;
    private Direction direction;

    int mouseX;
    int mouseY;

    int mouseOffsetX;
    int mouseOffsetY;


    public void render(ScreenEvent.Render event) {
        if (component == null) return;

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        Area area = component.area;

        RENDER.drawOutline(area);

        if (tooltip) {
            FONT.drawShadowedText(
                    "x: " + area.x + "\n" +
                    "y: " + area.y + "\n" +
                    "width: " + area.width + "\n" +
                    "height: " + area.height + "\n",
                    mouseX + 15, mouseY, area.z,
                    shift ? new Color(0xFF88B781, true) : new Color(-1), false, 0.8F);
        }

        if (direction != null) {
            int size = 2;

            float x = area.x - size;
            float y = area.y - size;
            float z = area.z;
            float x2 = area.x2 - size;
            float y2 = area.y2 - size;

            Color first = new Color(0xCC000000, true);
            Color second = shift ? new Color(0xFF88B781, true) : new Color(0xCCFFFFFF, true);

            switch (direction) {
                case BOTTOM_RIGHT:
                    RENDER.drawOutline(new Area(x2, y2, z, size * 2, size * 2), first, second);
                    break;
                case TOP_RIGHT:
                    RENDER.drawOutline(new Area(x2, y, z, size * 2, size * 2), first, second);
                    break;
                case BOTTOM_LEFT:
                    RENDER.drawOutline(new Area(x, y2, z, size * 2, size * 2), first, second);
                    break;
                case TOP_LEFT:
                    RENDER.drawOutline(new Area(x, y, z, size * 2, size * 2), first, second);
                    break;
                case TOP:
                    RENDER.drawOutline(new Area(x + area.width / 2, y, z, size * 2, size * 2), first, second);
                    break;
                case BOTTOM:
                    RENDER.drawOutline(new Area(x2 - area.width / 2, y2, z, size * 2, size * 2), first, second);
                    break;
                case LEFT:
                    RENDER.drawOutline(new Area(x, y2 - area.height / 2, z, size * 2, size * 2), first, second);
                    break;
                case RIGHT:
                    RENDER.drawOutline(new Area(x2, y + area.height / 2, z, size * 2, size * 2), first, second);
                    break;
            }
        }
    }

    public void input(ScreenEvent.MouseInput event) {
        if (component == null) return;

        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();

        if (event.getType() == MouseClickType.CLICKED) {
            direction = component.area.direction(mouseX, mouseY);
            mouseOffsetX = (int) (mouseX - component.area.x);
            mouseOffsetY = (int) (mouseY - component.area.y);
        }
    }

    public void clickMove(ScreenEvent.MouseClickMove event) {
        if (component == null) return;

        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (!component.area.onArea(mouseX, mouseY, 100, 100)) return;

        if (mouseOffsetX == 0 && mouseOffsetY == 0) {
            mouseOffsetX = (int) (mouseX - component.area.x);
            mouseOffsetY = (int) (mouseY - component.area.y);
        }

        if (type == EditingTypes.MOVE) {

            component.area.move(mouseX, mouseY, mouseOffsetX, mouseOffsetY);
        }
        if (type == EditingTypes.RESIZE) {

            if (component instanceof Label) {
                ((Label<?>) component).size = resizeText(((Label<?>) component).size, component.area, mouseX, mouseY, mouseOffsetX, mouseOffsetY);
                return;
            }

            if (direction == null) {
                direction = component.area.direction(mouseX, mouseY);
            }

            component.area.resize(direction, mouseX, mouseY);
        }
    }

    public void move(ScreenEvent.MouseMove event) {
        if (component == null) return;

        this.mouseX = event.getMouseX();
        this.mouseY = event.getMouseY();
    }

    private int tooltipTicks = 0;

    public void tick(ScreenEvent.Tick event) {
        if (component == null) return;

        if (this.component.area.onArea(mouseX, mouseY)) {
            tooltipTicks++;
            if (tooltipTicks >= 20) {
                tooltipTicks = 0;
                tooltip = true;
            }
        } else {
            tooltip = false;
        }
    }

    public float resizeText(float size, Area area, int mouseX, int mouseY, int offsetX, int offsetY) {
        float deltaX = mouseX - (area.x + offsetX);
        float deltaY = mouseY - (area.y + offsetY);
        float scaleFactor = 0.001f;

        float newSize = size + ((deltaX + deltaY) * scaleFactor);

        float minSize = 0.2f;
        float maxSize = 10f;
        newSize = Math.max(minSize, Math.min(newSize, maxSize));

        return newSize;
    }

}
