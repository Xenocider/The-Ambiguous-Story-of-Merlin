package workexpIT.merlin.entities;

import workexpIT.merlin.Output;
import workexpIT.merlin.graphics.Drawer;

import java.nio.ByteBuffer;
import java.util.List;

public class Entity {

    public Entity(int x, int y, String name, ByteBuffer[] sprites) {
        this.x=x;
        this.y=y;
        this.name = name;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ByteBuffer[] getSprites() {
        return sprites;
    }

    protected int x;
    protected int y;
    protected String name;
    public ByteBuffer[] sprites;
    public static int numOfSprites;

}
