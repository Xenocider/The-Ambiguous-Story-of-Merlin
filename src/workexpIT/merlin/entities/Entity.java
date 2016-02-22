package workexpIT.merlin.entities;

import workexpIT.merlin.Output;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class Entity {

    public static final int MOVE_UP = 0;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_DOWN = 2;


    protected int state;
    protected int level;
    protected int x;
    protected int y;
    protected String name;
    public BufferedImage[] sprites;
    public static int numOfSprites;
    public boolean moving;
    public int[] lastLoc = new int[2];

    public static final int STATE_NEUTRAL = 0;
    public static final int STATE_FRIENDLY = 1;
    public static final int STATE_AGGRESSIVE = 2;

    public int spriteId;


    public Entity(int x, int y, String name, int state, int level, BufferedImage[] sprites) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.level = level;
        this.state = state;
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

    public BufferedImage[] getSprites() {
        return sprites;
    }

    public int getLevel() {
        return level;
    }

    public int getState() {
        return state;
    }

    public void runAI() {

    }

    public void move(int direction) {
        //0 = up, 1 = right, 2 = down, 3 = left
            switch (direction) {
                case MOVE_UP:
                    try {
                        if (WorldData.tiles[getX()][getY() - 1].movingOnToTile(this)) {
                            lastLoc[0] = getX();
                            lastLoc[1] = getY();
                            setY(getY() - 1);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Output.write("Entity at edge of map!");
                    } catch (NullPointerException e) {
                        Output.write("Entity at edge of map!");
                    }
                    break;
                case MOVE_RIGHT:
                    try {
                        if (WorldData.tiles[getX() + 1][getY()].movingOnToTile(this)) {
                            lastLoc[0] = getX();
                            lastLoc[1] = getY();
                            setX(getX() + 1);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Output.write("Entity at edge of map!");
                    } catch (NullPointerException e) {
                        Output.write("Entity at edge of map!");
                    }
                    break;
                case MOVE_DOWN:
                    try {
                        if (WorldData.tiles[getX()][getY() + 1].movingOnToTile(this)) {
                            lastLoc[0] = getX();
                            lastLoc[1] = getY();
                            setY(getY() + 1);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Output.write("Entity at edge of map!");
                    } catch (NullPointerException e) {
                        Output.write("Entity at edge of map!");
                    }
                    break;
                case MOVE_LEFT:
                    try {
                        if (WorldData.tiles[getX() - 1][getY()].movingOnToTile(this)) {
                            lastLoc[0] = getX();
                            lastLoc[1] = getY();
                            setX(getX() - 1);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Output.write("Entity at edge of map!");
                    } catch (NullPointerException e) {
                        Output.write("Entity at edge of map!");
                    }
                    break;
             }
    }
}
