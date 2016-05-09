package workexpIT.merlin.entities;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.attacks.Attack;
import workexpIT.merlin.data.EventReader;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;
import workexpIT.merlin.graphics.JavaDrawer;

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
    public int[] downWalkingSpritesId;
    public int[] upWalkingSpritesId;
    public int[] rightWalkingSpritesId;
    public int[] leftWalkingSpritesId;
    public int upSpriteId;
    public int downSpriteId;
    public int rightSpriteId;
    public int leftSpriteId;

    public BufferedImage[] downWalkingSprites;
    public BufferedImage[] upWalkingSprites;
    public BufferedImage[] rightWalkingSprites;
    public BufferedImage[] leftWalkingSprites;
    public BufferedImage upSprite;
    public BufferedImage downSprite;
    public BufferedImage rightSprite;
    public BufferedImage leftSprite;

    public String dialog = null;

    public boolean talkable = false;
    public boolean freeze = false;


    public Entity(int x, int y, String name, int state, int level, BufferedImage sprites) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.level = level;
        this.state = state;
        this.sprites = sprites;
        health = (int)(baseHealth+factorHealth*level);
        healthMax = (int)(baseHealth+factorHealth*level);
        mana = (int)(baseMana+factorMana*level);
        manaMax = (int)(baseMana+factorMana*level);
        manaRegen = (int)(baseManaRegen+factorManaRegen*level);
        fortitude = (int)(baseFortitude+factorFortitude*level);
        speed = (int)(baseSpeed+factorSpeed*level);
        lastLoc[0] = x;
        lastLoc[1] = y;
    }

    public void loadSprites() {
        downWalkingSprites = new BufferedImage[downWalkingSpritesId.length];
        upWalkingSprites = new BufferedImage[upWalkingSpritesId.length];
        rightWalkingSprites = new BufferedImage[rightWalkingSpritesId.length];
        leftWalkingSprites = new BufferedImage[leftWalkingSpritesId.length];
        upSprite = JavaDrawer.scale(ImageReader.cropImage(sprites,upSpriteId*16,0,16,32),JavaDrawer.scale,JavaDrawer.scale);
        downSprite = JavaDrawer.scale(ImageReader.cropImage(sprites,downSpriteId*16,0,16,32),JavaDrawer.scale,JavaDrawer.scale);
        rightSprite = JavaDrawer.scale(ImageReader.cropImage(sprites,rightSpriteId*16,0,16,32),JavaDrawer.scale,JavaDrawer.scale);
        leftSprite = JavaDrawer.scale(ImageReader.cropImage(sprites,leftSpriteId*16,0,16,32),JavaDrawer.scale,JavaDrawer.scale);
        for (int i = 0; i < upWalkingSpritesId.length; i++) {
            upWalkingSprites[i] = JavaDrawer.scale(ImageReader.cropImage(sprites, (upWalkingSpritesId[i]) * 16, 0, 16, 32),JavaDrawer.scale,JavaDrawer.scale);
        }
        for (int i = 0; i < downWalkingSpritesId.length; i++) {
            downWalkingSprites[i] = JavaDrawer.scale(ImageReader.cropImage(sprites, (downWalkingSpritesId[i]) * 16, 0, 16, 32),JavaDrawer.scale,JavaDrawer.scale);
        }
        for (int i = 0; i < rightWalkingSpritesId.length; i++) {
            rightWalkingSprites[i] = JavaDrawer.scale(ImageReader.cropImage(sprites, (rightWalkingSpritesId[i]) * 16, 0, 16, 32),JavaDrawer.scale,JavaDrawer.scale);
        }
        for (int i = 0; i < leftWalkingSpritesId.length; i++) {
            leftWalkingSprites[i] = JavaDrawer.scale(ImageReader.cropImage(sprites, (leftWalkingSpritesId[i]) * 16, 0, 16, 32),JavaDrawer.scale,JavaDrawer.scale);
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
        if (!freeze) {
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
                        Output.write("Entity " + this.getName() + "at edge of map!a");
                    } catch (NullPointerException e) {
                        Output.write("Entity " + this.getName() + "at edge of map!b");
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
                        Output.write("Entity " + this.getName() + "at edge of map!c");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Output.write("Entity " + this.getName() + "at edge of map!d");
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
                        Output.write("Entity " + this.getName() + "at edge of map!e");
                    } catch (NullPointerException e) {
                        Output.write("Entity " + this.getName() + "at edge of map!f");
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
                        Output.write("Entity " + this.getName() + "at edge of map!g");
                    } catch (NullPointerException e) {
                        Output.write("Entity " + this.getName() + "at edge of map!h");
                    }
                    break;
            }
        }
        return false;
    }

    public boolean moveOverride(int direction) {
        facing = direction;
        //0 = up, 1 = right, 2 = down, 3 = left
        switch (direction) {
            case MOVE_UP:
                try {
                    WorldData.tiles[getX()][getY() - 1].movingOnToTile(this);
                        lastMove = MOVE_UP;
                        setY(getY() - 1);
                        moving = true;
                        return true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!i");
                } catch (NullPointerException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!j");
                }
                break;
            case MOVE_RIGHT:
                try {
                    WorldData.tiles[getX() + 1][getY()].movingOnToTile(this);
                        lastMove = MOVE_RIGHT;
                        setX(getX() + 1);
                        moving = true;
                        return true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!k");
                } catch (NullPointerException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!l");
                }
                break;
            case MOVE_DOWN:
                try {
                    WorldData.tiles[getX()][getY() + 1].movingOnToTile(this);
                        lastMove = MOVE_DOWN;
                        setY(getY() + 1);
                        moving = true;
                        return true;

                } catch (ArrayIndexOutOfBoundsException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!m");
                } catch (NullPointerException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!n");
                }
                break;
            case MOVE_LEFT:
                try {
                    WorldData.tiles[getX() - 1][getY()].movingOnToTile(this);
                        lastMove = MOVE_LEFT;
                        setX(getX() - 1);
                        moving = true;
                        return true;

                } catch (ArrayIndexOutOfBoundsException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!o");
                } catch (NullPointerException e) {
                    Output.write("Entity " + this.getName() + "at edge of map!p");
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


    public void freeze(boolean f) {
        Output.write("FREEZE");
        freeze = f;
        synchronized(Merlin.eventHandler.syncObject) {
            Merlin.eventHandler.condition = true;
            Merlin.eventHandler.syncObject.notify();
        }
    }

    public void playerInteraction() {
        Output.write(dialog + " " + talkable);
        if (dialog != null && talkable){GameLoop.displayDialog(dialog);}

        /*if (state == STATE_NEUTRAL) {
            //Do Nothing it is not an NPC
        }
        if (state == STATE_FRIENDLY) {
            Output.write(dialog);
            GameLoop.displayDialog(dialog);
        }*/
        if (state == STATE_AGGRESSIVE) {
            GameLoop.startBattle(WorldData.getPlayer(),this);
        }
    }
}
