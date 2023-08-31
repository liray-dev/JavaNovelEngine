package jne.editor.utils;

import jne.editor.nodes.NodeEditor;
import jne.editor.nodes.NodeStorage;
import jne.engine.api.IWrapper;
import jne.engine.constants.Colors;
import jne.engine.constants.Direction;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.widgets.Component;
import lombok.Data;

import static jne.engine.constants.Colors.textCursorColor;
import static jne.engine.constants.Colors.toolColor;

@Data
public class NodeMovableComponent implements IWrapper {

    private final NodeEditor editor;

    public Component component;
    public EditingTypes type = EditingTypes.NONE;
    public boolean tooltip = false;
    public Direction direction;

    int mouseX;
    int mouseY;

    int mouseOffsetX;
    int mouseOffsetY;

    public void render(ScreenEvent.Render event) {
        if (component == null) {

            if (getType() == EditingTypes.NODE) {
                if (!createdNodeArea.isEmpty() && createdNodeArea.x2 != 0 && createdNodeArea.y2 != 0) {
                    RENDER.color(textCursorColor, () -> {
                        RENDER.drawQuad(createdNodeArea.x, createdNodeArea.y, editor.Z_LEVEL, createdNodeArea.x2, createdNodeArea.y2);
                    });
                }
            }

        } else {
            Area area = component.getArea();

            RENDER.drawOutline(area);

            if (direction != null) {
                int size = 2;

                float x = area.x - size;
                float y = area.y - size;
                float z = area.z;
                float x2 = area.x2 - size;
                float y2 = area.y2 - size;


                Area position = null;

                switch (direction) {
                    case BOTTOM_RIGHT:
                        position = new Area(x2, y2, z, 1, 1);
                        break;
                    case TOP_RIGHT:
                        position = new Area(x2, y, z, 1, 1);
                        break;
                    case BOTTOM_LEFT:
                        position = new Area(x, y2, z, 1, 1);
                        break;
                    case TOP_LEFT:
                        position = new Area(x, y, z, 1, 1);
                        break;
                    case TOP:
                        position = new Area(x + area.width / 2, y, z, 1, 1);
                        break;
                    case BOTTOM:
                        position = new Area(x2 - area.width / 2, y2, z, 1, 1);
                        break;
                    case LEFT:
                        position = new Area(x, y2 - area.height / 2, z, 1, 1);
                        break;
                    case RIGHT:
                        position = new Area(x2, y + area.height / 2, z, 1, 1);
                        break;
                }

                if (position != null) {
                    nodeLineArea.x = position.x;
                    nodeLineArea.y = position.y;
                    System.out.println(direction.name());
                    FONT.drawColoredShadowedText("o", position.x, position.y, position.z, Colors.whiteOutline, true, 1F);
                }

            }

            if (getType() == EditingTypes.NODE_LINE) {
                if (!nodeLineArea.isEmpty() && nodeLineArea.x2 != 0 && nodeLineArea.y2 != 0) {
                    RENDER.drawLine(nodeLineArea.x, nodeLineArea.y, nodeLineArea.x2, nodeLineArea.y2, nodeLineArea.z, 2F);
                }
            }

        }
    }

    public void input(ScreenEvent.MouseInput event) {
        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();


        if (component == null) {
            if (getType() == EditingTypes.NODE) {
                if (event.getType() == MouseClickType.CLICKED) {
                    createdNodeArea.x = mouseX;
                    createdNodeArea.y = mouseY;
                    return;
                }
                if (event.getType() == MouseClickType.RELEASED) {
                    if (!createdNodeArea.isEmpty() && createdNodeArea.x2 != 0 && createdNodeArea.y2 != 0) {
                        NodeStorage storage = editor.handler.storage;
                        storage.add(GRAPHICS.button()
                                .area(createdNodeArea)
                                .color(toolColor)
                                .label(GRAPHICS.label().text("Scene " + (storage.getComponents().size() + 1)).centered(true).build(), true)
                                .onPress((component, type) -> {
                                    if (type == MouseClickType.CLICKED) {
                                        editor.nodeMovableComponent.component = component;
                                        editor.lastClickedComponent = component;
                                        editor.currentEditingType = EditingTypes.NODE_LINE;
                                        editor.currentToolID = "";
                                        editor.recreate();
                                        setType(EditingTypes.NODE_LINE);
                                    }
                                })
                                .onFailPress((component, type) -> {
                                    if (type == MouseClickType.CLICKED) {
                                        if (editor.nodeMovableComponent.component == component) {
                                            editor.nodeMovableComponent.component = null;
                                            editor.lastClickedComponent = null;
                                            editor.currentEditingType = EditingTypes.NONE;
                                            editor.currentToolID = "";
                                            setType(EditingTypes.NONE);
                                        }
                                    }
                                })
                                .build());
                        createdNodeArea = new Area();
                    }
                }
            }
        } else {
            if (event.getType() == MouseClickType.CLICKED) {
                mouseOffsetX = (int) (mouseX - component.getArea().x);
                mouseOffsetY = (int) (mouseY - component.getArea().y);
            }
        }
    }

    public Area createdNodeArea = new Area();
    public Area nodeLineArea = new Area();

    public void clickMove(ScreenEvent.MouseClickMove event) {
        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (component == null) {
            // Create Node
            if (getType() == EditingTypes.NODE) {
                createdNodeArea.x2 = mouseX;
                createdNodeArea.y2 = mouseY;
                createdNodeArea.width = createdNodeArea.x2 - createdNodeArea.x;
                createdNodeArea.height = createdNodeArea.y2 - createdNodeArea.y;
                return;
            }
        } else {
            if (getType() == EditingTypes.NODE_LINE) {
                nodeLineArea.x2 = mouseX;
                nodeLineArea.y2 = mouseY;
                nodeLineArea.width = nodeLineArea.x2 - nodeLineArea.x;
                nodeLineArea.height = nodeLineArea.y2 - nodeLineArea.y;
            }
        }
    }

    public void move(ScreenEvent.MouseMove event) {
        if (component == null) return;

        this.mouseX = event.getMouseX();
        this.mouseY = event.getMouseY();

        this.direction = component.getArea().direction(mouseX, mouseY);
    }

    private int tooltipTicks = 0;

    public void tick(ScreenEvent.Tick event) {
        if (component == null) return;

        if (this.component.getArea().onArea(mouseX, mouseY)) {
            tooltipTicks++;
            if (tooltipTicks >= 20) {
                tooltipTicks = 0;
                tooltip = true;
            }
        } else {
            tooltip = false;
        }
    }

}
