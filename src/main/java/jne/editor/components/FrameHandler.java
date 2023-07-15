package jne.editor.components;

import jne.engine.constants.EventPriority;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.screens.widgets.Button;
import jne.editor.utils.Frame;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.stream.Collectors;

import static jne.engine.constants.Colors.brightBarColor;
import static jne.engine.constants.Colors.clickedToolColor;

public class FrameHandler extends ComponentsListener {

    protected final int Z_LEVEL = -50;

    public final SceneEditor sceneEditorScreen;

    public FrameHandler(SceneEditor sceneEditorScreen) {
        this.sceneEditorScreen = sceneEditorScreen;
    }

    public int currentButton = Integer.MIN_VALUE;

    @Override
    public void init() {
        int id = sceneEditorScreen.componentStore.getCurrentFrameID();

        add(GRAPHICS.button()
                .id(0)
                .area(new Area(60, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text("-").color(clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(brightBarColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        if (id - 1 < 0) return;
                        sceneEditorScreen.componentStore.deleteFrame(sceneEditorScreen.componentStore.getCurrentFrameID());
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id(1)
                .area(new Area(85, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text(id - 1 < 0 ? "..." : String.valueOf(id - 1)).color(clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(brightBarColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        if (id - 1 < 0) return;
                        sceneEditorScreen.componentStore.selectFrame(sceneEditorScreen.componentStore.getFrame(id - 1));
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id(2)
                .area(new Area(110, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text(String.valueOf(id)).color(clickedToolColor.brighter().brighter().brighter()).size(0.5F).centered(true).build(), true)
                .color(brightBarColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id(3)
                .area(new Area(135, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text(!sceneEditorScreen.componentStore.hasFrame(id + 1) ? "..." : String.valueOf(id + 1)).color(clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(brightBarColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        if (!sceneEditorScreen.componentStore.hasFrame(id + 1)) return;
                        sceneEditorScreen.componentStore.selectFrame(sceneEditorScreen.componentStore.getFrame(id + 1));
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        add(GRAPHICS.button()
                .id(4)
                .area(new Area(160, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text("+").color(clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(brightBarColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        if (sceneEditorScreen.componentStore.hasFrame(id + 1)) return;
                        Frame plusFrame = sceneEditorScreen.componentStore.getFrame(id + 1);
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            plusFrame.setComponents(sceneEditorScreen.componentStore.getComponents().stream().map(Component::clone).collect(Collectors.toList()));
                        }
                        sceneEditorScreen.componentStore.selectFrame(plusFrame);
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());
    }

    @Override
    public void update() {
        List<Button> buttons = getComponentsByID(currentButton, Button.class);
        buttons.forEach(it -> it.color = clickedToolColor);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void render(ScreenEvent.Render event) {
        super.render(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void input(ScreenEvent.MouseInput event) {
        super.input(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void move(ScreenEvent.MouseMove event) {
        super.move(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void keyboard(ScreenEvent.Keyboard event) {
        if (event.getType() == KeyboardType.STOP) return;

        int button = event.getButton();

        if (button == Keyboard.KEY_LEFT) {
            //DO
        }
        if (button == Keyboard.KEY_RIGHT) {
            //DO
        }

    }


}
