package jne.engine.screens.listeners;

import jne.engine.constants.EnumScriptType;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.events.types.ScreenEvent;
import jne.engine.events.types.ScriptEvent;
import jne.engine.screens.components.Component;
import jne.engine.utils.IComponent;
import jne.engine.utils.IComponentsListener;
import jne.engine.utils.IWrapper;

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
        getComponents().forEach(IComponent::tick);
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

    private final List<IComponent> components = new ArrayList<>();

    final public <T extends IComponent> T add(T component) {
        components.add(component);

        ScriptEvent.Init init = new ScriptEvent.Init((Component) component);
        component.getScriptContainer().run(EnumScriptType.INIT, init);
        init.post();
        return component;
    }

    final public <T extends IComponent> void remove(T component) {
        components.remove(component);

    }

    final public void setComponents(List<IComponent> list) {
        this.components.addAll(list);
    }

    final public List<IComponent> getComponents() {
        return new ArrayList<>(components);
    }

    final public void clearComponents() {
        this.components.clear();
    }

    final public <T extends IComponent> List<T> getComponentsByType(Class<T> componentType) {
        return components.stream()
                .filter(componentType::isInstance)
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    final public <T extends IComponent> List<T> getComponentsByID(String id, Class<T> componentType) {
        return components.stream()
                .filter(componentType::isInstance)
                .filter(it -> it.getID().equals(id))
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    final public <T extends IComponent> List<T> getComponentsByID(String id) {
        return components.stream()
                .filter(it -> it.getID().equals(id))
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
