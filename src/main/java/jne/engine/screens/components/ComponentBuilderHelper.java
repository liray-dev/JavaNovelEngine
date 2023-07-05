package jne.engine.screens.components;

import jne.engine.renderer.Graphics;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ComponentBuilderHelper {

    public ComponentBuilderHelper(Object object) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();

        

        System.out.println(Arrays.toString(methods));

    }

    public static void main(String[] args) {
        new ComponentBuilderHelper(Graphics.getInstance().button().self());
    }

}
