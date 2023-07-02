package jne.engine.utils;


public interface IPressable<T> {

    void onPress(T component, MouseClickType type);

}
