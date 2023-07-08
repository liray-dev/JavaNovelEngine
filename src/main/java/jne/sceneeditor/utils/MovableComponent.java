package jne.sceneeditor.utils;

import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;
import jne.engine.utils.IWrapper;
import lombok.Data;

import java.awt.*;

@Data
public class MovableComponent implements IWrapper {

    private final Component component;
    private EditingTypes type = EditingTypes.NONE;
    private final Color toolColor = new Color(0x383838);
    private final Color barColor = new Color(0x181818);
    private final Color outlineColor = new Color(0xCC000000, true);

    int mouseOffsetX;
    int mouseOffsetY;

    public void render(ScreenEvent.Render event) {
        if (component == null) return;

        Area area = component.area;

        RENDER.color(outlineColor, () -> {
            //TOP
            RENDER.drawQuad(area.x, area.y, area.z, area.x2 + 1, area.y + 1);
            //RIGHT
            RENDER.drawQuad(area.x2, area.y, area.z, area.x2 + 1, area.y2 + 1);
            //UNDER
            RENDER.drawQuad(area.x2, area.y2, area.z, area.x + 1, area.y2 + 1);
            //LEFT
            RENDER.drawQuad(area.x, area.y2, area.z, area.x + 1, area.y + 1);
        });

    }

    public void input(ScreenEvent.MouseInput event) {
        if (component == null) return;

        if (event.getType() == MouseClickType.CLICKED) {
            mouseOffsetX = (int) (event.getMouseX() - component.area.x);
            mouseOffsetY = (int) (event.getMouseY() - component.area.y);
        }
    }

    public void clickMove(ScreenEvent.MouseClickMove event) {
        if (component == null) return;

        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();

        if (type == EditingTypes.MOVE) {
            if (!component.area.onArea(mouseX, mouseY, 100, 100)) return;
            component.area.move(mouseX, mouseY, mouseOffsetX, mouseOffsetY);
        }
        if (type == EditingTypes.RESIZE) {
            System.out.println(component.area.direction(mouseX, mouseY).name());
        }
    }

    public void move(ScreenEvent.MouseMove event) {
        if (component == null) return;
    }


    public void tick(ScreenEvent.Tick event) {
        if (component == null) return;
    }

}
