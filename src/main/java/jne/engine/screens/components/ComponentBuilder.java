

package jne.engine.screens.components;

import jne.engine.utils.IComponent;

/**
 * Implementation of a handy builder for creating multifunctional UI components.
 * @param <SELF> BUILDER
 * @param <T> COMPONENT
 */
@SuppressWarnings("unchecked")
public abstract class ComponentBuilder<SELF extends ComponentBuilder<?, T>, T extends IComponent> {

    protected ComponentBuilder() {
        instance = create();
    }

    public T build() {
        return instance();
    }

    protected abstract T create();

    private final T instance;

    public SELF self() {
        return (SELF) this;
    }

    protected T instance() {
        return instance;
    }

}
