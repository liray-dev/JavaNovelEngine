package jne.engine.utils;

public interface IComponentsListener {

    void render(float partialTicks);

    void mouseMove(int mouseX, int mouseY);

    void mouseClicked(int mouseX, int mouseY, int mouseButton);

    void mouseReleased(int mouseX, int mouseY, int mouseButton);

    void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick);

    void keyTyped(char typedChar, int keyCode);

    void tick();

}
