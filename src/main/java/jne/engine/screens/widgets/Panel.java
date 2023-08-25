package jne.engine.screens.widgets;

import jne.engine.constants.Colors;
import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.BasicLayout;
import jne.engine.api.IComponent;
import jne.engine.api.IScrollable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

public class Panel<SELF extends Panel<SELF>> extends BasicLayout<SELF> implements IScrollable {

    protected Color color;
    private IComponent scrollHandler;
    private boolean draggable;
    private int scrollVertical;
    private int scrollHorizontal;

    private float scrollVerticalProgress;

    /**
     * Some offsets
     */
    protected int xOffset;
    protected int yOffset;

    protected float maxContentX;
    protected float maxContentY;

    protected boolean wrapContent;

    protected Panel() {

    }

    @Override
    public String addComponent(int depth, @NotNull IComponent component) {
        String id = super.addComponent(depth, component);
        component.getArea().z = area.z;
        component.setOnPress((self, type) -> {
            if (type != MouseClickType.CLICKED) return;
            this.scrollHandler = (IComponent) self;
        });
        maxContentY = Math.max(maxContentY, component.getAbsoluteY());
        maxContentX = Math.max(maxContentX, component.getAbsoluteX());

        if (wrapContent) {
            this.setWidth(this.getContentWidth() + xOffset * 2);
            this.setHeight(this.getContentHeight() + yOffset * 2);
        }
        return id;
    }

    @Override
    public IComponent removeComponent(String id) {
        IComponent removed = super.removeComponent(id);

        Collection<IComponent> content = this.getContent().getContent().values();
        Optional<IComponent> any = content.stream().findAny();

        if (any.isPresent()) {
            area.x2 = any.get().getAbsoluteX();
            area.y2 = any.get().getAbsoluteY();
            maxContentY = maxContentX = 0;
            for (IComponent component : content) {
                area.x2 = Math.max(area.x2, component.getAbsoluteX());
                area.y2 = Math.max(area.y2, component.getAbsoluteY());
                maxContentY = Math.max(maxContentY, component.getAbsoluteY());
                maxContentX = Math.max(maxContentX, component.getAbsoluteX());
            }
        } else {
            area.x = area.x2 = 0;
            area.y = area.y2 = 0;
            maxContentY = maxContentX = 0;
        }

        if (wrapContent) {
            this.setWidth(this.getContentWidth() + xOffset * 2);
            this.setHeight(this.getContentHeight() + yOffset * 2);
        }

        return removed;
    }

    @Override
    public void onRender(float partialTicks) {
        RENDER.color(color, () -> {
            RENDER.drawQuad(area.x, area.y, area.z, area.x2, area.y2);
        });
        if (needScroll()) {
            RENDER.color(Colors.toolColor.darker(), () -> {
                RENDER.drawQuad(area.x2 - getSliderSize(), area.y, area.z, area.x2, area.y2);
            });

            float position = area.height * scrollVerticalProgress;
            float size = 10;

            RENDER.color(Colors.clickedToolColor, () -> {
                RENDER.drawQuad(
                        area.x2 - getSliderSize(),
                        Math.min(area.y2 - size, Math.max(area.y, area.y + position)),
                        area.z,
                        area.x2,
                        Math.min(area.y2, (area.y + position + size))
                );
            });
        }
        RENDER.scissor(getX(), getY(), getContentWidth(), getContentHeight(), () -> {
            super.onRender(partialTicks);
        });
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
    public void onWheel(int mouseX, int mouseY, int value) {
        super.onWheel(mouseX, mouseY, value);

        scrollVerticalProgress = Math.max(0F, Math.min(1F, scrollVerticalProgress + ((value > 0F) ? 0.1F : -0.1F)));
        doSlider();
    }

    @Override
    public void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime) {
        super.onMoveClick(mouseX, mouseY, mouseButton, lastClickTime);

        float x = area.x2 - getSliderSize();
        float y = area.y;
        float width = area.x2;
        float height = area.y2;

        Area sliderArea = new Area(x, y, area.z, width, height);

        if (sliderArea.onArea(mouseX, mouseY)) {
            scrollVerticalProgress = (mouseY - area.y) / area.height;
            doSlider();
        }
        if (!needScroll()) {
            getContent().getAll().values().forEach(iComponent -> iComponent.getArea().offsetY = 0);
        }
    }

    public void doSlider() {
        getContent().getAll().values().forEach(iComponent -> {
            Area area = iComponent.getArea();
            area.offsetY = ((maxContentY - this.area.height) * scrollVerticalProgress);
        });
    }

    public float getSliderSize() {
        return 15;
    }
    
    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setScrollHandler(IComponent handler) {
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
        return area.x2 - area.x;
    }

    @Override
    public float getContentHeight() {
        return area.y2 - area.y;
    }

    public boolean needScroll() {
        return area.height < maxContentY;
    }

    public SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends Panel<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new Panel();
        }

        public SELF wrapContent() {
            instance().wrapContent = true;
            return self();
        }

        public SELF color(Color color) {
            instance().color = color;
            return self();
        }

    }

}
