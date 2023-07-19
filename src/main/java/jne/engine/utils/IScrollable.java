package jne.engine.utils;

import jne.engine.screens.components.Component;
import jne.engine.utils.IComponent;
import org.jetbrains.annotations.Nullable;

/**
 * API for containers which were made to be scrollable. Should store {@link Component} as scrollHandler
 * and apply the resulting scroll shifts.
 */
public interface IScrollable {

    void setScrollHandler(IComponent handler);

    @Nullable
    IComponent getScrollHandler();

    default boolean scrollEnabled() {
        return getScrollHandler() != null;
    }

    int getScrollVertical();

    int getScrollHorizontal();

    void setScrollVertical(int value);

    void setScrollHorizontal(int value);

    float getContentWidth();

    float getContentHeight();

    default void addScrollVertical(int value) {
        setScrollVertical(getScrollVertical() + value);
    }

    default void addScrollHorizontal(int value) {
        setScrollHorizontal(getScrollHorizontal() + value);
    }
}
