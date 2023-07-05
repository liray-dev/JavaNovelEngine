package jne.engine.screens.widgets;

import jne.engine.constants.KeyboardType;
import jne.engine.screens.components.Component;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TextBox<SELF extends TextBox<SELF>> extends Component<SELF> {

    public Color textColor = new Color(0xFFFFFF);
    public Color ghostColor = new Color(0x969696);
    public Color barColor = new Color(0x383838);
    public int cursorPosition = 0;
    public String text = "";
    public String ghostText = "";

    @Override
    public void onRender(float partialTicks) {
        RENDER.color(barColor, () -> {
            RENDER.drawQuad(area.x, area.y, area.z, area.x2, area.y2);
        });
        if (text != null && !text.isEmpty()) {
            float x = (area.x + FONT.getWidth(text.substring(0, 1)) / 2);
            float y = (area.y + (area.height - FONT.getHeight(text)) / 2);
            FONT.drawText(text, x, y, area.z, textColor, false);

            if (active && ticks >= 10) {
                FONT.drawText("|", (x + (cursorPosition * (FONT.getWidth(text) / text.length())) - (FONT.size / 2)), y, area.z, new Color(0x9D9D9D), false);
            }
        } else {
            if (ghostText != null && !ghostText.isEmpty()) {
                float y = (area.y + (area.height - FONT.getHeight(ghostText)) / 2);
                FONT.drawText(ghostText, (area.x), y, area.z, ghostColor, false);
            }
        }

    }

    @Override
    public void onKeyTyped(int keyCode, char typedChar, KeyboardType type) {
        if (type == KeyboardType.STOP) {
            return;
        }

        if (keyCode == Keyboard.KEY_BACK) {
            if (cursorPosition > 0) {
                text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                cursorPosition--;
            }
            return;
        }

        if (keyCode == Keyboard.KEY_RIGHT) {
            if (cursorPosition < text.length()) {
                cursorPosition++;
            }
            return;
        }
        if (keyCode == Keyboard.KEY_LEFT) {
            if (cursorPosition > 0) {
                cursorPosition--;
            }
            return;
        }

        if (typedChar != '\0') {
            text = text.substring(0, cursorPosition) + typedChar + text.substring(cursorPosition);
            cursorPosition++;
        }
    }

    @Override
    public void onTick() {

    }

    protected SELF self() {
        return (SELF) this;
    }

    public static class Builder<SELF extends Builder<SELF, T>, T extends TextBox<T>> extends Component.Builder<SELF, T> {

        @Override
        public T create() {
            return (T) new TextBox();
        }

        public SELF text(String text) {
            instance().text = text;
            return self();
        }

        public SELF ghostText(String text) {
            instance().ghostText = text;
            return self();
        }

    }


}
