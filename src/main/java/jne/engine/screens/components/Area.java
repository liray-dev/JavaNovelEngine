package jne.engine.screens.components;

import jne.engine.constants.Direction;
import jne.engine.errors.DebugManager;
import jne.engine.serializer.ISerializable;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

public class Area implements Cloneable, ISerializable {

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

    public Area(float x, float y) {
        this.x = x;
        this.y = y;

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

    public float offsetX, offsetY = 0;

    public void moveTo(float mouseX, float mouseY) {
        this.x = mouseX;
        this.y = mouseY;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    public void move(float mouseX, float mouseY, float offsetX, float offsetY) {
        float deltaX = mouseX - (x + offsetX);
        float deltaY = mouseY - (y + offsetY);
        this.x += deltaX;
        this.y += deltaY;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (this.x % 2 != 0) {
                this.x += 1;
            }
            if (this.y % 2 != 0) {
                this.y += 1;
            }
        }
        this.x2 = x + width;
        this.y2 = y + height;
    }

    public void resize(Direction direction, float mouseX, float mouseY) {
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        switch (direction) {
            case TOP:
                this.y = mouseY;
                break;
            case LEFT:
                this.x = mouseX;
                break;
            case RIGHT:
                this.x2 = mouseX;
                break;
            case BOTTOM:
                this.y2 = mouseY;
                break;
            case TOP_RIGHT:
                this.x2 = mouseX;
                this.y = mouseY;

                if (shift) {
                    float max = Math.max(x2 - x, y2 - y);
                    this.x2 = x + max;
                    this.y = y2 - max;

                    this.width = max;
                    this.height = max;
                    return;
                }

                break;
            case BOTTOM_LEFT:
                this.x = mouseX;
                this.y2 = mouseY;

                if (shift) {
                    float max = Math.max(x2 - x, y2 - y);
                    this.x = x2 - max;
                    this.y2 = y + max;

                    this.width = max;
                    this.height = max;
                    return;
                }

                break;
            case TOP_LEFT:
                this.x = mouseX;
                this.y = mouseY;
                if (shift) {
                    float max = Math.max(x2 - x, y2 - y);
                    this.x = x2 - max;
                    this.y = y2 - max;

                    this.width = max;
                    this.height = max;
                    return;
                }
                break;
            case BOTTOM_RIGHT:
                this.x2 = mouseX;
                this.y2 = mouseY;
                if (shift) {
                    float max = Math.max(x2 - x, y2 - y);
                    this.width = max;
                    this.height = max;
                    this.x2 = x + width;
                    this.y2 = y + height;
                    return;
                }
                break;
        }
        this.width = x2 - x;
        this.height = y2 - y;
    }

    public Direction direction(int mouseX, int mouseY) {
        Area center = getCenter();

        float deltaX = mouseX - center.x;
        float deltaY = mouseY - center.y;

        float angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX)) - 22.5F;

        if (angle < 0) {
            angle += 360;
        }

        float sectorAngle = 360 / Direction.values().length;
        int sectorIndex = (int) (angle / sectorAngle);

        return Direction.values()[sectorIndex];
    }

    public Area offset(float offsetX, float offsetY, Direction direction) {
        float x;
        float y;
        switch (direction) {
            case BOTTOM:
                x = this.x + offsetX;
                y = this.y2 + offsetY;
                break;
            case RIGHT:
                x = this.x2 + offsetX;
                y = this.y + +offsetY;
                break;
            default:
                x = this.x;
                y = this.y;
        }
        return new Area(x, y, this.z, this.width, this.height);
    }

    public Area offset(float offsetX, float offsetY, float width, float height, Direction direction) {
        float x;
        float y;
        switch (direction) {
            case BOTTOM:
                x = this.x + offsetX;
                y = this.y2 + offsetY;
                break;
            case RIGHT:
                x = this.x2 + offsetX;
                y = this.y + +offsetY;
                break;
            default:
                x = this.x;
                y = this.y;
        }
        return new Area(x, y, this.z, width, height);
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


    @Override
    public Area clone() {
        try {
            return (Area) super.clone();
        } catch (CloneNotSupportedException e) {
            DebugManager.error(new Exception("Failed to copy object coordinates"));
            return new Area();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("x", this.x);
        json.put("y", this.y);
        json.put("x2", this.x2);
        json.put("y2", this.y2);

        json.put("z", this.z);

        json.put("width", this.width);
        json.put("height", this.height);

        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        this.x = json.getFloat("x");
        this.y = json.getFloat("y");
        this.x2 = json.getFloat("x2");
        this.y2 = json.getFloat("y2");

        this.z = json.getFloat("z");

        this.width = json.getFloat("width");
        this.height = json.getFloat("height");
    }
}
