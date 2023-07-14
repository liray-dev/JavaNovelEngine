package jne.sceneeditor.screens;

import jne.engine.constants.EventPriority;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.screens.widgets.Button;
import jne.engine.texture.TextureContainer;
import jne.sceneeditor.utils.EditingTypes;
import jne.sceneeditor.utils.Frame;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FrameMenu extends ComponentsListener {

    protected final int Z_LEVEL = -50;

    public final SceneEditor sceneEditorScreen;

    public FrameMenu(SceneEditor sceneEditorScreen) {
        this.sceneEditorScreen = sceneEditorScreen;
    }

    public int currentButton = Integer.MIN_VALUE;

    public Button left;
    public Button right;

    public Button currentFrame;
    public Button leftFrame;
    public Button rightFrame;


    @Override
    public void init() {
        int id = sceneEditorScreen.componentStore.currentFrame.getId();

        left = add(GRAPHICS.button()
                .id(0)
                .area(new Area(60, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text("-").color(sceneEditorScreen.clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(sceneEditorScreen.barColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        if (id - 1 < 0) return;
                        sceneEditorScreen.componentStore.deleteFrame(id);
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        leftFrame = add(GRAPHICS.button()
                .id(1)
                .area(new Area(85, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text(id - 1 < 0 ? "..." : String.valueOf(id - 1)).color(sceneEditorScreen.clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(sceneEditorScreen.barColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        if (id - 1 < 0) return;
                        sceneEditorScreen.componentStore.selectFrame(sceneEditorScreen.componentStore.getFrame(id - 1));
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        currentFrame = add(GRAPHICS.button()
                .id(2)
                .area(new Area(110, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text(String.valueOf(id)).color(sceneEditorScreen.clickedToolColor.brighter().brighter().brighter()).size(0.5F).centered(true).build(), true)
                .color(sceneEditorScreen.barColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        rightFrame = add(GRAPHICS.button()
                .id(3)
                .area(new Area(135, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text(!sceneEditorScreen.componentStore.hasFrame(id + 1) ? "..." : String.valueOf(id + 1)).color(sceneEditorScreen.clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(sceneEditorScreen.barColor)
                .onPress((component, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        if (!sceneEditorScreen.componentStore.hasFrame(id + 1)) return;
                        sceneEditorScreen.componentStore.selectFrame(sceneEditorScreen.componentStore.getFrame(id + 1));
                        currentButton = component.id;
                        recreate();
                    }
                })
                .build());

        right = add(GRAPHICS.button()
                .id(4)
                .area(new Area(160, height - 25, Z_LEVEL, 25, 25))
                .label(GRAPHICS.label().text("+").color(sceneEditorScreen.clickedToolColor.brighter()).size(0.5F).centered(true).build(), true)
                .color(sceneEditorScreen.barColor)
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
        buttons.forEach(it -> it.color = sceneEditorScreen.clickedToolColor);
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
