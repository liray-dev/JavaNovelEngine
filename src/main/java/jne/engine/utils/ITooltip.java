package jne.engine.utils;

import jne.engine.screens.components.Component;

public interface ITooltip<T extends Component> {

     void onTooltip(T component, int mouseX, int mouseY, float partialTicks);

}
