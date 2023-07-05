package jne.engine.utils;

import jne.engine.constants.KeyboardType;
import jne.engine.constants.MouseClickType;
import jne.engine.screens.components.Area;

public interface IComponent {

    void onRender(float partialTicks);

    void onFocused(int mouseX, int mouseY);

    void onClicked(int mouseX, int mouseY, int button, MouseClickType type);

    void onFailClick(int mouseX, int mouseY, int button, MouseClickType type);

    void onMoveClick(int mouseX, int mouseY, int mouseButton, long lastClickTime);

    void onKeyTyped(int keyCode, char typedChar, KeyboardType type);

    void onTick();

    void setVisibility(boolean visibility);

    void setArea(Area area);

    Area getArea();

}
