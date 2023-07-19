package jne.engine.utils;

import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.screens.components.Area;
import jne.engine.screens.components.Component;
import jne.engine.scripts.ScriptContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IComponent extends Cloneable, IComponentsListener {

    String getID();

    void setID(String id);

    boolean visible();

    void setVisibility(boolean visibility);

    float getAbsoluteX();

    float getX();

    void setX(float x);

    default void shiftX(float value) {
        setX(getX() + value);
    }

    float getAbsoluteY();

    float getY();

    void setY(float y);

    default void shiftY(float value) {
        setY(getY() + value);
    }

    float getWidth();

    void setWidth(float width);

    default void growWidth(float value) {
        setWidth(getWidth() + value);
    }

    float getHeight();

    void setHeight(float height);

    default void growHeight(float value) {
        setHeight(getHeight() + value);
    }

    Area getArea();

    void setArea(Area area);

    int getDepth();

    void setDepth(int value);

    void markDirty();

    boolean checkUpdates();

    void update();

    default boolean hasParent() {
        return getParent() != null;
    }

    @Nullable IComponent getParent();

    void setParent(@NotNull IComponent component);

    String getScript();

    void setScript(String script);

    ScriptContainer getScriptContainer();

    void onRender(float partialTicks);

    void onFocused(int mouseX, int mouseY);

    void onClicked(int mouseX, int mouseY, int button, MouseClickType type);

    void onFailClick(int mouseX, int mouseY, int button, MouseClickType type);

    void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime);

    void onKeyTyped(int keyCode, char typedChar, KeyboardType type);

    void onTick();
}
