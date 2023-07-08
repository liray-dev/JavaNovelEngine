package jne.engine.screens.components;

import jne.engine.constants.Direction;

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

        // Just crutch
    }

    public void move(int mouseX, int mouseY, int offsetX, int offsetY) {
        float deltaX = mouseX - (x + offsetX);
        float deltaY = mouseY - (y + offsetY);
        this.x += deltaX;
        this.y += deltaY;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    public Direction direction(int mouseX, int mouseY) {
        Area center = getCenter();

        float deltaX = mouseX - center.x;
        float deltaY = mouseY - center.y;

        float angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (angle < 0) {
            angle += 360;
        }

        float sectorAngle = 360 / Direction.values().length;
        int sectorIndex = (int) (angle / sectorAngle);

        return Direction.values()[sectorIndex];
    }

    public Area offset(float offsetX, float offsetY) {
        float x = this.x2 + offsetX;
        float y = this.y2 + offsetY;
        return new Area(x, y, this.z, this.width, this.height);
    }

    public Area offset(float offsetX, float offsetY, float offsetWidth, float offsetHeight) {
        float x = this.x2 + offsetX;
        float y = this.y2 + offsetY;
        return new Area(x, y, this.z, offsetWidth, offsetHeight);
    }

    public Area getCenter() {
        return new Area(this.x + (this.width / 2), this.y + (height / 2), this.z, this.x2, this.y2);
    }

    public boolean onArea(float mouseX, float mouseY) {
        boolean flag = mouseX >= x && mouseX <= x2;
        boolean flag2 = mouseY >= y && mouseY <= y2;
        return flag && flag2;
    }

    public boolean onArea(float mouseX, float mouseY, float offsetX, float offsetY) {
        boolean flag = mouseX >= x - offsetX && mouseX <= x2 + offsetX;
        boolean flag2 = mouseY >= y - offsetY && mouseY <= y2 + offsetY;
        return flag && flag2;
    }

    public boolean isEmpty() {
        return width == 0 && height == 0 && x == 0 && y == 0;
    }

}
