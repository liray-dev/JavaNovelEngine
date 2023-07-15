package jne.engine.screens.widgets;

import jne.engine.constants.KeyboardType;
import jne.engine.screens.components.Component;
import jne.engine.screens.components.ComponentConstructor;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static jne.engine.constants.Colors.*;

public class TextBox<SELF extends TextBox<SELF>> extends Component<SELF> {

    public int cursorPositionX = 0;
    public String text = "";
    public String ghostText = "";
    public float size = 1F;

    @Override
    public void onRender(float partialTicks) {
        RENDER.color(toolColor, () -> {
            RENDER.drawQuad(area.x, area.y, area.z, area.x2, area.y2);
        });
        if (text != null && !text.isEmpty()) {
            float x = (area.x + FONT.getWidth(text.substring(0, 1)) / 2);
            float y = (area.y + (area.height - FONT.getHeight(text)) / 2);
            FONT.drawText(text, x, y, area.z, textColor, false, size);

            if (active && ticks >= 10) {
                int length = text.substring(0, cursorPositionX).length();
                String spaceString = new String(new char[length]).replace('\0', ' ');
                FONT.drawText(spaceString + "|", x - FONT.getWidth(text.substring(0, 1)) / (2 / size), y, area.z, textCursorColor, false, size);
            }
        } else {
            if (ghostText != null && !ghostText.isEmpty()) {
                float y = (area.y + (area.height - FONT.getHeight(ghostText)) / 2);
                FONT.drawText(ghostText, area.x, y, area.z, ghostColor, false, size);
            }
        }
    }

    @Override
    public void onKeyTyped(int keyCode, char typedChar, KeyboardType type) {
        if (type == KeyboardType.STOP) {
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
            active = false;
            return;
        }

        if (keyCode == Keyboard.KEY_BACK) {
            if (cursorPositionX > 0) {
                text = text.substring(0, cursorPositionX - 1) + text.substring(cursorPositionX);
                cursorPositionX--;
            }
            return;
        }

        if (keyCode == Keyboard.KEY_RIGHT) {
            if (cursorPositionX < text.length()) {
                cursorPositionX++;
            }
            return;
        }
        if (keyCode == Keyboard.KEY_LEFT) {
            if (cursorPositionX > 0) {
                cursorPositionX--;
            }
            return;
        }

        if (typedChar != '\0' && typedChar != '\u0001' && typedChar != '\t') {
            text = text.substring(0, cursorPositionX) + typedChar + text.substring(cursorPositionX);
            cursorPositionX++;
        }
    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends TextBox<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new TextBox();
        }

        @ComponentConstructor(text = "Text", example = "example: message")
        public SELF text(String text) {
            instance().text = text;
            return self();
        }

        @ComponentConstructor(text = "Ghost Text", example = "example: 'example: ...'")
        public SELF ghostText(String text) {
            instance().ghostText = text;
            return self();
        }

        @ComponentConstructor(text = "Text Size", example = "example: 1.0")
        public SELF size(float size) {
            instance().size = size;
            return self();
        }

    }


}
