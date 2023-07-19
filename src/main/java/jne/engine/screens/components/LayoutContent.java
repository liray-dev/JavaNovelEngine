

package jne.engine.screens.components;

import jne.engine.utils.IComponent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LayoutContent<T extends IComponent> {

    private final Map<String, T> content = new HashMap<>();

    public static <T extends IComponent> LayoutContent<T> create() {
        return new LayoutContent<>();
    }

    public static <T extends IComponent> LayoutContent<T> withContent(Map<String, T> content) {
        LayoutContent<T> builder = LayoutContent.create();
        builder.content.putAll(content);
        return builder;
    }

    public LayoutContent<T> putComponent(String id, T component) {
        component.setID(id);
        content.put(id, component);
        return this;
    }

    public LayoutContent<T> putComponent(T component) {
        return putComponent(UUID.randomUUID().toString(), component);
    }

    public void clear() {
        content.clear();
    }

    public T remove(String id) {
        return content.remove(id);
    }

    public T get(String id) {
        return content.get(id);
    }

    public Map<String, T> getContent() {
        return Collections.unmodifiableMap(content);
    }
}
