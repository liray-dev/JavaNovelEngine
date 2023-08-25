package jne.engine.api;

import jne.engine.screens.widgets.Component;

public interface ITooltip<T extends Component> {

     void onTooltip(T component, int mouseX, int mouseY, float partialTicks);

}
