package jne.engine.screens.components.constructor;

import jne.editor.utils.constructors.AbstractComponentConstructor;
import jne.engine.debug.DebugManager;
import jne.engine.screens.widgets.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ComponentConstructorHelper {

    public final Component.Builder builder;
    public final HashMap<String, MethodConstructor> methodHashMap = new HashMap<>();

    public ComponentConstructorHelper(Component.Builder object) {
        this.builder = object;
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            ComponentConstructor annotation = method.getAnnotation(ComponentConstructor.class);
            if (annotation != null) {
                Class<?>[] methodTypes = getMethodTypes(method);
                String infoText = annotation.text() + getMethodTypesNames(methodTypes);

                try {
                    Constructor<?> constructor = annotation.option().getDeclaredConstructor();
                    constructor.setAccessible(true);
                    AbstractComponentConstructor option = (AbstractComponentConstructor) constructor.newInstance();
                    methodHashMap.put(method.getName(), new MethodConstructor(method, annotation.builder(), option, methodTypes, annotation.example(), infoText));
                } catch (Exception e) {
                    DebugManager.debug("Error of creating an auxiliary button " + annotation.option().getSimpleName());
                }

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
