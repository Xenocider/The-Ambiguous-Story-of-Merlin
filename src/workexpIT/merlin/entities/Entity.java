package workexpIT.merlin.entities;

import workexpIT.merlin.Output;
import workexpIT.merlin.attacks.Attack;
import workexpIT.merlin.data.ImageReader;
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

    public int leveledHealth,leveledMana,leveledManaRegen,leveledFortitude,leveledSpeed;
    public float factorHealth = 20;
    public float factorMana = 15;
    public float factorManaRegen = 1;
    public float factorFortitude = 1;
    public float factorSpeed = 1;
    public int baseHealth = 100;
    public int baseMana = 100;
    public float baseManaRegen = 10;
    public int baseFortitude = 10;
    public int baseSpeed = 10;

    protected int state;
    protected int level;
    protected int x;
    protected int y;
    protected String name;
    public BufferedImage[] sprites;
    public BufferedImage battleSprite;
    public static int numOfSprites;
    public boolean moving;
    public int[] lastLoc = new int[2];
    public Attack[] attacks = new Attack[6];

    public static final int STATE_NEUTRAL = 0;
    public static final int STATE_FRIENDLY = 1;
    public static final int STATE_AGGRESSIVE = 2;

    public int spriteId;

    public int health = 100;
    public int healthMax = 100;
    public int mana = 100;
    public int manaMax = 100;
    public int manaRegen = 10;
    public int fortitude = 10;
    public int speed = 10;


    public Entity(int x, int y, String name, int state, int level, BufferedImage[] sprites) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.level = level;
        this.state = state;
        this.sprites = sprites;
        battleSprite = ImageReader.loadImage("resources/graphics/charactersprites/"+this.getClass().getSimpleName()+"/battle.png");
        Output.write("resources/graphics/charactersprites/"+this.getClass().getSimpleName()+"/battle.png");
        health = (int)(baseHealth+factorHealth*level);
        healthMax = (int)(baseHealth+factorHealth*level);
        mana = (int)(baseMana+factorMana*level);
        manaMax = (int)(baseMana+factorMana*level);
        manaRegen = (int)(baseManaRegen+factorManaRegen*level);
        fortitude = (int)(baseFortitude+factorFortitude*level);
        speed = (int)(baseSpeed+factorSpeed*level);
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

    public void regenMana() {
        mana = mana + manaRegen;
        if (mana > manaMax) {
            mana = manaMax;
        }
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
                        Output.write("Entity at edge of map!a");
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
                        Output.write("Entity at edge of map!a");
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
                        Output.write("Entity at edge of map!a");
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
                        Output.write("Entity at edge of map!a");
                    } catch (NullPointerException e) {
                        Output.write("Entity at edge of map!");
                    }
                    break;
             }
    }
}
