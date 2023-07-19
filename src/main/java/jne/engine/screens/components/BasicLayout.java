package jne.engine.screens.components;

import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.utils.IComponent;
import jne.engine.utils.ILayout;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class BasicLayout<SELF extends BasicLayout<SELF>> extends Component<SELF> implements ILayout<SELF> {

    private final LayoutContent<SELF> content = new LayoutContent<>();
    // for rendering
    private final NavigableSet<SELF> sorted = new TreeSet<>(((o1, o2) -> {
        if (o1.getDepth() == o2.getDepth()) {
            return o1.getID().compareTo(o2.getID());
        }
        return o1.getDepth() - o2.getDepth();
    }));

    protected BasicLayout() {
    }

    public BasicLayout(Area area) {
        this.area = area;
    }

    public String addComponent(final int depth, final String id, @NotNull final SELF component) {
        component.setDepth(depth);
        this.putComponent(id, component);
        return component.getID();
    }

    public void putComponent(String id, @NotNull SELF component) {
        component.setParent(this);
        content.putComponent(id, component);
        sorted.add(component);
    }

    public SELF getComponent(String id) {
        return content.getContent().get(id);
    }

    public SELF removeComponent(String id) {
        SELF removed = content.remove(id);
        sorted.remove(removed);
        return removed;
    }

    @Override
    public void setContent(@NotNull LayoutContent<? extends IComponent> newContent) throws ClassCastException {
        clear();
        newContent.getContent().forEach((id, component) -> putComponent(id, (SELF) component));
    }

    @Override
    public @NotNull LayoutContent<SELF> getContent() {
        return content;
    }

    @Override
    public int size() {
        return content.getContent().size();
    }

    @Override
    public void clear() {
        content.clear();
        sorted.clear();
    }

    public SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends BasicLayout<T>> extends Component.Builder<SELF, T> {

    }

    @Override
    public void onRender(float partialTicks) {
        sorted.forEach(component -> {
            component.render(partialTicks);
        });
    }

    @Override
    public void onFocused(int mouseX, int mouseY) {
        sorted.forEach(component -> {
            component.mouseMove(mouseX, mouseY);
        });
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int button, MouseClickType type) {
        sorted.forEach(component -> {
            switch (type) {
                case CLICKED:
                    component.mouseClicked(mouseX, mouseY, button);
                    break;
                case RELEASED:
                    component.mouseReleased(mouseX, mouseY, button);
                    break;
            }
        });
    }

    @Override
    public void onKeyTyped(int keyCode, char typedChar, KeyboardType type) {
        sorted.forEach(component -> {
            component.keyTyped(typedChar, keyCode, type);
        });
    }

    @Override
    public void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime) {
        sorted.forEach(component -> {
            component.mouseClickMove(mouseX, mouseY, mouseButton, lastClickTime);
        });
    }

    @Override
    public void onTick() {
        sorted.forEach(Component::tick);
    }

}
