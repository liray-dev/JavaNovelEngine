

package jne.engine.utils;

import jne.engine.screens.components.LayoutContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface ILayout<T extends IComponent> extends IComponent {

    default String addComponent(int depth, @NotNull T component) {
        return addComponent(depth, UUID.randomUUID().toString(), component);
    }

    default String addComponent(@NotNull T component) {
        return addComponent(0, component);
    }

    String addComponent(int depth, String id, @NotNull T component);

    default String addComponent(String id, @NotNull T component) {
        return addComponent(id, component);
    }

    void putComponent(String id, @NotNull T component);


    @Nullable
    T getComponent(String id);


    @Nullable
    T removeComponent(String id);


    void setContent(@NotNull LayoutContent<? extends IComponent> newContent) throws ClassCastException;


    @NotNull
    LayoutContent<T> getContent();

    @SuppressWarnings("unchecked")
    default void setContent(@NotNull Collection<? extends IComponent> newContent) {
        clear();
        newContent.forEach(component -> addComponent(component.getDepth(), (T) component));
    }

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    void clear();

}
