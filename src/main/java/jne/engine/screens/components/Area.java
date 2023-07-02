package jne.engine.screens.components;

public class Area {

    public float x, y, x2, y2, z;
    public float width, height;

    public Area() {
        this.x = 0;
        this.y = 0;

        this.x2 = 0;
        this.y2 = 0;

        this.z = 0;

        this.width = 0;
        this.height = 0;
    }

    public Area(float x, float y, float z, float width, float height) {
        this.x = x;
        this.y = y;
        this.x2 = x + width;
        this.y2 = y + height;
        this.z = z;
        this.width = width;
        this.height = height;
    }

    public Area(float x, float y, float x2, float y2, float z, boolean flag) {
        this.x = x;
        this.y = y;

        this.x2 = x2;
        this.y2 = y2;

        this.z = z;

        this.width = x2 - x;
        this.height = y2 - y;
    }

    public void move(int mouseX, int mouseY, int offsetX, int offsetY) {
        float deltaX = mouseX - (x + offsetX);
        float deltaY = mouseY - (y + offsetY);
        this.x += deltaX;
        this.y += deltaY;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    public Area offset(float offsetX, float offsetY) {
        float x = this.x2 + offsetX;
        float y = this.y2 + offsetY;
        return new Area(x, y, this.width, this.height, this.z);
    }

    public Area offset(float offsetX, float offsetY, float offsetWidth, float offsetHeight) {
        float x = this.x2 + offsetX;
        float y = this.y2 + offsetY;
        return new Area(x, y, offsetWidth, offsetHeight, this.z);
    }

    public Area getCenter() {
        return new Area(this.x + (this.width / 2), this.y + (height / 2), this.x2, this.y2, this.z);
    }

    public boolean onArea(float mouseX, float mouseY) {
        boolean flag = mouseX >= x && mouseX <= x2;
        boolean flag2 = mouseY >= y && mouseY <= y2;
        return flag && flag2;
    }

    public boolean isEmpty() {
        return width == 0 && height == 0 && x == 0 && y == 0;
    }

}
