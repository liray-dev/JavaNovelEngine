package jne.editor.scenes;

import jne.editor.utils.EditingTypes;
import jne.editor.utils.Frame;
import jne.editor.utils.MovableComponent;
import jne.engine.serializer.ISerializable;
import jne.engine.constants.EventPriority;
import jne.engine.constants.MouseClickType;
import jne.engine.errors.DebugManager;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Area;
import jne.engine.screens.widgets.Component;
import jne.engine.screens.listeners.ComponentsListener;
import jne.engine.api.IComponent;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FrameStorage extends ComponentsListener implements ISerializable {

    protected final int Z_LEVEL = -1000;

    public final HashSet<Frame> frames = new HashSet<>();
    public final FrameHandler frameHandler;

    public Component lastClickedComponent = null;

    public int id = 0;
    public Frame currentFrame = new Frame(id);
    public MovableComponent movableComponent = new MovableComponent(null);

    public FrameStorage(FrameHandler frameHandler) {
        this.frameHandler = frameHandler;
        this.frames.add(currentFrame);
    }

    @Override
    public void init() {
        // Just debug
        this.isInit = true;


//        Panel<? extends Panel<?>> panel = GRAPHICS.panel().area(new Area(10, 10, Z_LEVEL, 600, 80)).wrapContent().onPress((c, t) -> {
//            if (t == MouseClickType.CLICKED) {
//                lastClickedComponent = c;
//            }
//        }).build();
//
//        Set<Map.Entry<String, Texture>> textures = TextureContainer.get().entrySet();
//
//        Area area = new Area(1, -51, 0, 50, 50);
//
//        for (Map.Entry<String, Texture> unit : textures) {
//            String key = unit.getKey();
//            Texture value = unit.getValue();
//            area = area.offset(0, 0, Direction.BOTTOM);
//            panel.addComponent(0, GRAPHICS.texture().texture(value).area(area).build());
//            panel.addComponent(1, GRAPHICS.label().area(area.offset(5, 20, Direction.RIGHT)).text(key).build());
//        }

        //add(panel);
    }

    public void selectComponent(Component component) {
        if (component == null) {
            this.lastClickedComponent = null;
        }
        this.movableComponent = new MovableComponent(component);
    }

    public void selectFrame(Frame frame) {
        if (this.currentFrame.equals(frame)) return;

        save();
        clear();
        load(frame);
    }

    public void save() {
        this.currentFrame.setComponents(getComponents());
    }

    public void clear() {
        this.clearComponents();
        this.lastClickedComponent = null;
        this.selectComponent(null);
    }

    public void load(Frame frame) {
        this.currentFrame = frame;
        this.setComponents(frame.getComponents());
    }

    public Frame getFrame(int id) {
        List<Frame> collect = frames.stream().filter(it -> it.getId() == id).collect(Collectors.toList());
        if (collect.size() != 1) {
            return createFrame(id);
        }
        return collect.get(0);
    }


    public boolean hasFrame(int id) {
        return frames.stream().anyMatch(it -> it.getId() == id);
    }

    public Frame createFrame() {
        Frame frame = new Frame(id++);
        this.frames.add(frame);
        return frame;
    }

    public Frame createFrame(int id) {
        Frame frame = new Frame(id);
        this.frames.add(frame);
        return frame;
    }

    public void deleteFrame(int id) {
        if (hasFrame(id)) {
            Frame frame = getFrame(id);
            if (hasFrame(id - 1)) {
                frames.remove(frame);
                Frame frame2 = getFrame(id - 1);
                clear();
                load(frame2);
            }
        }
    }

    public Frame getCurrentFrame() {
        return currentFrame;
    }

    public int getCurrentFrameID() {
        return currentFrame.getId();
    }

    public EditingTypes getEditType() {
        return frameHandler.sceneUnitScreen.currentEditingType;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void render(ScreenEvent.Render event) {
        super.render(event);
        movableComponent.render(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void input(ScreenEvent.MouseInput event) {
        super.input(event);
        movableComponent.input(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void clickMove(ScreenEvent.MouseClickMove event) {
        super.clickMove(event);
        movableComponent.clickMove(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void move(ScreenEvent.MouseMove event) {
        super.move(event);
        movableComponent.move(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void tick(ScreenEvent.Tick event) {
        super.tick(event);
        movableComponent.tick(event);
        movableComponent.setType(getEditType());

        if (movableComponent.getComponent() == null && lastClickedComponent != null) {
            this.selectComponent(lastClickedComponent);
            lastClickedComponent = null;
        }

        if (!isInit) {
            init();
        }

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void wheel(ScreenEvent.Wheel event) {
        super.wheel(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void keyboard(ScreenEvent.Keyboard event) {
        int button = event.getButton();
        if (button == Keyboard.KEY_ESCAPE) {
            selectComponent(null);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onCloseSubScreen(ScreenEvent.Close event) {
        if (event.getScreen().getClass().equals(SettingComponentScreen.class)) {
            SettingComponentScreen screen = (SettingComponentScreen) event.getScreen();
            if (screen.init) {
                if (screen.component == null) return;
                IComponent component = screen.component.clone();

                float centerX = WINDOW.displayWidth / 2F;
                float centerY = WINDOW.displayHeight / 2F;

                component.setArea(new Area(centerX - 100,  centerY - 100, Z_LEVEL, 200, 200));
                component.setOnPress((c, type) -> {
                    if (type == MouseClickType.CLICKED) {
                        lastClickedComponent = (Component) c;
                    }
                });
                add(component);

                // Debug
                String name = component.getClass().getSimpleName();
                DebugManager.debug(name + " was added!");
            }
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        frames.forEach(frame -> {
            json.put(String.valueOf(frame.getId()), frame.toJson());
        });

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        frames.clear();

        for (String key : json.keySet()) {
            JSONObject frameJson = json.getJSONObject(key);


            Frame frame = new Frame(Integer.getInteger(key));
            frame.fromJson(frameJson);

            frames.add(frame);
        }
    }

}
