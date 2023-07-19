package jne.engine.screens.widgets;

import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.errors.DebugManager;
import jne.engine.screens.components.BasicLayout;
import jne.engine.screens.components.Component;
import jne.engine.utils.IScrollable;
import jne.engine.utils.IComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.*;


public class Panel<SELF extends Panel<SELF>> extends BasicLayout<SELF> implements IScrollable {

    private IComponent scrollHandler;
    private boolean draggable;
    private int scrollVertical;
    private int scrollHorizontal;
    private float contentMinX;
    private float contentMaxX;
    private float contentMinY;
    private float contentMaxY;

    /**
     * Some offsets
     */
    protected int xOffset;
    protected int yOffset;

    protected boolean wrapContent;

    protected Panel() {

    }

    @Override
    public String addComponent(int depth, @NotNull SELF component) {
        String id = super.addComponent(depth, component);
        contentMinX = Math.min(contentMinX, component.getX());
        contentMaxX = Math.max(contentMaxX, component.getX() + component.getWidth());
        contentMinY = Math.min(contentMinY, component.getY());
        contentMaxY = Math.max(contentMaxY, component.getY() + component.getHeight());
        if (wrapContent) {
            this.setWidth(this.getContentWidth() + xOffset * 2);
            this.setHeight(this.getContentHeight() + yOffset * 2);
        }
        return id;
    }

    @Override
    public SELF removeComponent(String id) {
        SELF removed = super.removeComponent(id);

        Collection<SELF> content = this.getContent().getContent().values();
        Optional<SELF> any = content.stream().findAny();

        if (any.isPresent()) {
            contentMinX = any.get().getX();
            contentMaxX = any.get().getX() + any.get().getWidth();
            contentMinY = any.get().getY();
            contentMaxY = any.get().getY() + any.get().getHeight();
            for (SELF component : content) {
                contentMinX = Math.min(contentMinX, component.getX());
                contentMaxX = Math.max(contentMaxX, component.getX() + component.getWidth());
                contentMinY = Math.min(contentMinY, component.getY());
                contentMaxY = Math.max(contentMaxY, component.getY() + component.getHeight());
            }
        } else {
            contentMinX = contentMaxX = 0;
            contentMinY = contentMaxY = 0;
        }

        if (wrapContent) {
            this.setWidth(this.getContentWidth() + xOffset * 2);
            this.setHeight(this.getContentHeight() + yOffset * 2);
        }

        return removed;
    }

    @Override
    public void onRender(float partialTicks) {
        super.onRender(partialTicks);
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int button, MouseClickType type) {
        super.onClicked(mouseX, mouseY, button, type);
    }

    @Override
    public void onFailClick(int mouseX, int mouseY, int button, MouseClickType type) {
        super.onFailClick(mouseX, mouseY, button, type);
    }

    @Override
    public void onFocused(int mouseX, int mouseY) {
        super.onFocused(mouseX, mouseY);
    }

    @Override
    public void onKeyTyped(int keyCode, char typedChar, KeyboardType type) {
        super.onKeyTyped(keyCode, typedChar, type);
    }

    @Override
    public void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime) {
        super.onMoveClick(mouseX, mouseY, mouseButton, lastClickTime);
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setScrollHandler(IComponent handler) {
        if (handler == null) {
            DebugManager.error(new Exception("ScrollHandler mustn't be null"));
            return;
        }
        scrollHandler = handler;
    }

    @Override
    public @Nullable IComponent getScrollHandler() {
        return scrollHandler;
    }

    @Override
    public int getScrollVertical() {
        return scrollVertical;
    }

    @Override
    public int getScrollHorizontal() {
        return scrollHorizontal;
    }

    @Override
    public void setScrollVertical(int value) {
        scrollVertical = value;
    }

    @Override
    public void setScrollHorizontal(int value) {
        scrollHorizontal = value;
    }

    @Override
    public float getContentWidth() {
        return contentMaxX - contentMinX;
    }

    @Override
    public float getContentHeight() {
        return contentMaxY - contentMinY;
    }

    public SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Panel<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new Panel();
        }

        public SELF setWrapContent() {
            instance().wrapContent = true;
            return self();
        }

    }

}
