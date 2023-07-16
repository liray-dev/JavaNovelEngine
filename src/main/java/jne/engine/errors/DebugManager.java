package jne.engine.errors;

import jne.engine.screens.components.Area;
import jne.engine.screens.widgets.Label;
import jne.engine.utils.IWrapper;
import jne.engine.utils.Util;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static jne.engine.constants.Colors.*;

public class DebugManager implements IWrapper {

    public static final TreeMap<Integer, DebugListener> errors = new TreeMap<>();
    private static final int limit = 5;
    private static final float Z_LEVEL = 5000;
    private static final Long maxLifeTime = 10000L;
    private static int id = 0;

    public static void error(Exception e) {
        error(e, maxLifeTime);
    }

    public static void error(Exception e, Long lifeTime) {
        error(e, lifeTime, errorColor);
    }

    public static void error(Exception e, long lifeTime, Color color) {
        e.printStackTrace(); // Console log

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw); // Write log
        errors.put(
                id++,
                new DebugListener(Util.getSystemTime(), lifeTime, e.getMessage(), sw.toString(), color)
        );
    }

    public static void debug(String message) {
        debug(message, maxLifeTime);
    }

    public static void debug(String message, Long lifeTime) {
        debug(message, lifeTime, debugColor);
    }

    public static void debug(String message, long lifeTime, Color color) {
        errors.put(
                id++,
                new DebugListener(Util.getSystemTime(), lifeTime, message, message, color)
        );
    }

    public static void render() {
        Set<Map.Entry<Integer, DebugListener>> values = new HashSet<>(errors.entrySet());
        List<Map.Entry<Integer, DebugListener>> sortedValues = values.stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())).collect(Collectors.toList());

        int x = 5;
        int y = 5;

        int i = -1;

        for (Map.Entry<Integer, DebugListener> error : sortedValues) {
            i++;

            DebugListener value = error.getValue();
            Integer key = error.getKey();

            if (Util.getSystemTime() - value.getInitTime() >= value.getLifeTime()) {
                errors.remove(key);
                continue;
            }

            String keyError = value.getKeyError();
            if (keyError == null) {
                keyError = "Unknown error, you broke something";
            }
            int finalX = x + (keyError.length() * 16) / 2;
            int finalY = 16;

            Area area = new Area(x, y, Z_LEVEL, finalX, finalY);

            try {
                if (i < limit) {
                    RENDER.color(new Color(0x80383838, true), () -> {
                        RENDER.drawQuad(area.x, area.y, Z_LEVEL, area.x2, area.y2);
                    });

                    Label<? extends Label<?>> label = GRAPHICS.label()
                            .area(new Area(area.x, area.y, area.z, 0, 0))
                            .text(keyError)
                            .size(0.5F)
                            .color(value.getColor())
                            .build();

                    label.onRender(1F);
                } else if (i == 5) {
                    RENDER.color(new Color(0x80383838, true), () -> {
                        RENDER.drawQuad(area.x, area.y, Z_LEVEL, area.x2, area.y2);
                    });

                    Label<? extends Label<?>> label = GRAPHICS.label()
                            .area(new Area(area.x, area.y, area.z, 0, 0))
                            .text("...")
                            .size(0.5F)
                            .color(value.getColor())
                            .build();

                    label.onRender(1F);
                } else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            y = y + finalY + 5;
        }


    }

}
