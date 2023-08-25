

package jne.engine.api;

import jne.engine.screens.components.LayoutContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface ILayout extends IComponent {

    default String addComponent(int depth, @NotNull IComponent component) {
        return addComponent(depth, UUID.randomUUID().toString(), component);
    }

    default String addComponent(@NotNull IComponent component) {
        return addComponent(0, component);
    }

    String addComponent(int depth, String id, @NotNull IComponent component);

    default String addComponent(String id, @NotNull IComponent component) {
        return addComponent(id, component);
    }

    void putComponent(String id, @NotNull IComponent component);


    @Nullable
    IComponent getComponent(String id);


    @Nullable
    IComponent removeComponent(String id);


    void setContent(@NotNull LayoutContent<? extends IComponent> newContent) throws ClassCastException;


    @NotNull
    LayoutContent<IComponent> getContent();

    @SuppressWarnings("unchecked")
    default void setContent(@NotNull Collection<? extends IComponent> newContent) {
        clear();
        newContent.forEach(component -> addComponent(component.getDepth(), (IComponent) component));
    }

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    void clear();

}
