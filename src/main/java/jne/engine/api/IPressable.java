package jne.engine.api;


import jne.engine.constants.MouseClickType;

public interface IPressable<T> {

    void onPress(T component, MouseClickType type);

}
