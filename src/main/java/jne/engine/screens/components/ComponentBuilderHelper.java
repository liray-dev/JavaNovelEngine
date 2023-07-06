package jne.engine.screens.components;

import jne.engine.renderer.Graphics;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ComponentBuilderHelper {

    public final Component.Builder builder;
    public final HashMap<String, MethodConstructor> methodHashMap = new HashMap<>();

    public ComponentBuilderHelper(Component.Builder object) {
        this.builder = object;
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            ComponentConstructor annotation = method.getAnnotation(ComponentConstructor.class);
            if (annotation != null) {
                Class<?>[] methodTypes = getMethodTypes(method);
                String infoText = annotation.text() + getMethodTypesNames(methodTypes);
                methodHashMap.put(method.getName(), new MethodConstructor(method, annotation.builder(), methodTypes, annotation.example(), infoText));
            }
        }

    }

    private Class<?>[] getMethodTypes(Method method) {
        return method.getParameterTypes();
    }

    private String getMethodTypesNames(Class<?>[] methodTypes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        int i = 0;
        int length = methodTypes.length;

        for (Class<?> clazz : methodTypes) {

            String name = clazz.getName();

            String[] split = name.split("\\.");

            stringBuilder.append(split[split.length - 1]);

            if (i < length - 1) {
                stringBuilder.append(",");
            }
            i++;
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }

}
