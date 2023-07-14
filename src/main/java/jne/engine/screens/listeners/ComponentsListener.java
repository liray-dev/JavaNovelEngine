package jne.engine.screens.listeners;

import jne.engine.constants.EnumScriptType;
import jne.engine.constants.EventPriority;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.types.ScriptEvent;
import jne.engine.events.utils.SubscribeEvent;
import jne.engine.screens.components.Component;
import jne.engine.screens.widgets.Button;
import jne.engine.screens.widgets.Label;
import jne.engine.screens.widgets.TexturedComponent;
import jne.engine.utils.IComponentsListener;
import jne.engine.utils.IWrapper;
import jne.sceneeditor.screens.components.settings.SettingButtonScreen;
import jne.sceneeditor.screens.components.settings.SettingLabelScreen;
import jne.sceneeditor.screens.components.settings.SettingTextureScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ComponentsListener implements IComponentsListener, IWrapper {

    public boolean isInit = false;
    public int width;
    public int height;

    public void init() {

    }

    public void close() {

    }

    public void update() {

    }

    @Override
    final public void render(float partialTicks) {
        getComponents().forEach(it -> it.render(partialTicks));
    }

    @Override
    final public void mouseMove(int mouseX, int mouseY) {
        getComponents().forEach(it -> it.mouseMove(mouseX, mouseY));
    }

    @Override
    final public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        getComponents().forEach(it -> it.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    final public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        getComponents().forEach(it -> it.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    final public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        getComponents().forEach(it -> it.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick));
    }

    @Override
    final public void keyTyped(char typedChar, int keyCode, KeyboardType type) {
        getComponents().forEach(it -> it.keyTyped(typedChar, keyCode, type));
    }

    @Override
    final public void tick() {
        getComponents().forEach(Component::tick);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        recreate();
    }

    final public void recreate() {
        if (!isInit) {
            clearComponents();
            init();
        }
        update();
    }

    final public void openSubscreen(ComponentsListener subscreen) {
        WINDOW.screenManager.addSubscreen(subscreen);
    }

    final public void sendUpdateInfo() {
        new ScreenEvent.UpdateInfo(this).post();
    }

    final public void killAll() {
        WINDOW.screenManager.clearSubscreens();
    }

    final public void kill() {
        WINDOW.screenManager.removeSubscreen(this);
    }

    /* Components management */

    private final List<Component> components = new ArrayList<Component>();

    final public <T extends Component> T add(T component) {
        components.add(component);

        ScriptEvent.Init init = new ScriptEvent.Init(component);
        component.scriptContainer.run(EnumScriptType.INIT, init);
        init.post();
        return component;
    }

    final public <T extends Component> void remove(T component) {
        components.remove(component);

    }

    final public void setComponents(List<Component> list) {
        this.components.addAll(list);
    }

    final public List<Component> getComponents() {
        return new ArrayList<>(components);
    }

    final public void clearComponents() {
        this.components.clear();
    }

    final public <T extends Component> List<T> getComponentsByType(Class<T> componentType) {
        return components.stream()
                .filter(componentType::isInstance)
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    final public <T extends Component> List<T> getComponentsByID(int id, Class<T> componentType) {
        return components.stream()
                .filter(componentType::isInstance)
                .filter(it -> it.id == id)
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    final public <T extends Component> List<T> getComponentsByID(int id) {
        return components.stream()
                .filter(it -> it.id == id)
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }


    public void render(ScreenEvent.Render event) {
        render(event.getPartialTick());
    }

    public void move(ScreenEvent.MouseMove event) {
        mouseMove(event.getMouseX(), event.getMouseY());
    }

    public void input(ScreenEvent.MouseInput event) {
        MouseClickType type = event.getType();
        if (type == MouseClickType.CLICKED) {
            mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
        }
        if (type == MouseClickType.RELEASED) {
            mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton());
        }
    }

    public void clickMove(ScreenEvent.MouseClickMove event) {
        mouseClickMove(event.getMouseX(), event.getMouseY(), event.getButton(), event.getLast());
    }

    public void keyboard(ScreenEvent.Keyboard event) {
        keyTyped(event.getCharacter(), event.getButton(), event.getType());
    }

    public void tick(ScreenEvent.Tick event) {
        tick();
    }

    public void onCloseSubScreen(ScreenEvent.Close event) {

    }

}
