package jne.editor.utils.constructors;

import jne.engine.api.IComponent;
import jne.engine.api.IWrapper;

public class AbstractComponentConstructor implements IWrapper {

    private float depth = 0;

    public IComponent collect() {
        return null;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public float getDepth() {
        return depth;
    }
}
