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

    public int facing = 2;

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
    public BufferedImage sprites;
    public BufferedImage battleSprite;
    public static int numOfSprites;
    public int[] lastLoc = new int[2];
    public Attack[] attacks = new Attack[6];
    public boolean moving = true;

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
    public int lastMove;
    public int animationStage = 0;
    public int maxAnimationStage = 3;

    //Sprites
    private final int[] downWalkingSpritesId = {3,4,3,5};
    private final int[] upWalkingSpritesId = {0,1,0,2};
    private final int[] rightWalkingSpritesId = {9,10,9,11};
    private final int[] leftWalkingSpritesId = {6,7,6,8};
    private final int upSpriteId = 0;
    private final int downSpriteId = 3;
    private final int rightSpriteId = 9;
    private final int leftSpriteId = 6;

    public BufferedImage[] downWalkingSprites = new BufferedImage[downWalkingSpritesId.length];
    public BufferedImage[] upWalkingSprites = new BufferedImage[upWalkingSpritesId.length];
    public BufferedImage[] rightWalkingSprites = new BufferedImage[rightWalkingSpritesId.length];
    public BufferedImage[] leftWalkingSprites = new BufferedImage[leftWalkingSpritesId.length];
    public BufferedImage upSprite;
    public BufferedImage downSprite;
    public BufferedImage rightSprite;
    public BufferedImage leftSprite;


    public Entity(int x, int y, String name, int state, int level, BufferedImage sprites) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.level = level;
        this.state = state;
        this.sprites = sprites;
        battleSprite = ImageReader.loadImage("resources/graphics/charactersprites/battle/"+getClass().getSimpleName()+".png");
        health = (int)(baseHealth+factorHealth*level);
        healthMax = (int)(baseHealth+factorHealth*level);
        mana = (int)(baseMana+factorMana*level);
        manaMax = (int)(baseMana+factorMana*level);
        manaRegen = (int)(baseManaRegen+factorManaRegen*level);
        fortitude = (int)(baseFortitude+factorFortitude*level);
        speed = (int)(baseSpeed+factorSpeed*level);
        loadSprites();
        lastLoc[0] = x;
        lastLoc[1] = y;
    }

    public void loadSprites() {
        upSprite = ImageReader.cropImage(sprites,upSpriteId*16,0,16,32);
        downSprite = ImageReader.cropImage(sprites,downSpriteId*16,0,16,32);
        rightSprite = ImageReader.cropImage(sprites,rightSpriteId*16,0,16,32);
        leftSprite = ImageReader.cropImage(sprites,leftSpriteId*16,0,16,32);
        for (int i = 0; i < upWalkingSpritesId.length; i++) {
            upWalkingSprites[i] = ImageReader.cropImage(sprites, (upWalkingSpritesId[i]) * 16, 0, 16, 32);
        }
        for (int i = 0; i < downWalkingSpritesId.length; i++) {
            downWalkingSprites[i] = ImageReader.cropImage(sprites, (downWalkingSpritesId[i]) * 16, 0, 16, 32);
        }
        for (int i = 0; i < rightWalkingSpritesId.length; i++) {
            rightWalkingSprites[i] = ImageReader.cropImage(sprites, (rightWalkingSpritesId[i]) * 16, 0, 16, 32);
        }
        for (int i = 0; i < leftWalkingSpritesId.length; i++) {
            leftWalkingSprites[i] = ImageReader.cropImage(sprites, (leftWalkingSpritesId[i]) * 16, 0, 16, 32);
        }
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

    public BufferedImage getSprites() {
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

    public boolean move(int direction) {
        facing = direction;
        //0 = up, 1 = right, 2 = down, 3 = left
            switch (direction) {
                case MOVE_UP:
                    try {
                        if (WorldData.tiles[getX()][getY() - 1].movingOnToTile(this)) {
                            lastMove = MOVE_UP;
                            setY(getY() - 1);
                            moving = true;
                            return true;
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
                            lastMove = MOVE_RIGHT;
                            setX(getX() + 1);
                            moving = true;
                            return true;
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
                            lastMove = MOVE_DOWN;
                            setY(getY() + 1);
                            moving = true;
                            return true;
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
                            lastMove = MOVE_LEFT;
                            setX(getX() - 1);
                            moving = true;
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Output.write("Entity at edge of map!a");
                    } catch (NullPointerException e) {
                        Output.write("Entity at edge of map!");
                    }
                    break;
             }
        return false;
    }

    public void changeAnimationStage() {
        //Output.write("Changing animation stage");
        animationStage = animationStage + 1;
        if (animationStage > maxAnimationStage) {
            animationStage = 0;
        }
    }
}
