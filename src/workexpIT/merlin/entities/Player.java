package workexpIT.merlin.entities;

import workexpIT.merlin.Output;
import workexpIT.merlin.attacks.*;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Player extends Entity{

    public int xp = 0;
    public int xpMax = 100;
    public int xpFactor = 10;

    //Sprites
    private final static int[] downWalkingSpritesId = {3,4,3,5};
    private final static int[] upWalkingSpritesId = {0,1,0,2};
    private final static int[] rightWalkingSpritesId = {9,10,9,11};
    private final static int[] leftWalkingSpritesId = {6,7,6,8};
    private final static int upSpriteId = 0;
    private final static int downSpriteId = 3;
    private final static int rightSpriteId = 9;
    private final static int leftSpriteId = 6;

    public static BufferedImage[] downWalkingSprites = new BufferedImage[downWalkingSpritesId.length];
    public static BufferedImage[] upWalkingSprites = new BufferedImage[upWalkingSpritesId.length];
    public static BufferedImage[] rightWalkingSprites = new BufferedImage[rightWalkingSpritesId.length];
    public static BufferedImage[] leftWalkingSprites = new BufferedImage[leftWalkingSpritesId.length];
    public static BufferedImage upSprite;
    public static BufferedImage downSprite;
    public static BufferedImage rightSprite;
    public static BufferedImage leftSprite;


    public static BufferedImage sprites = ImageReader.loadImage("resources/graphics/charactersprites/player.png");

    public static void loadSprites() {
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

    public Player(int x, int y, int level) {
        super(x, y, "player", Entity.STATE_NEUTRAL, level, new BufferedImage[]{sprites});
        attacks[0] = new Punch();
        attacks[1] = new Fireball();
        attacks[2] = new DrainLife();
        attacks[3] = new DrainMana();
        attacks[4] = new Flood();
        attacks[5] = new LifeBend();
    }

    public void addXP(int xpGained) {
        xp = xp + xpGained;
        if (xp >= xpMax) {
            xp = xp - xpMax;
            level = level + 1;
            xpMax = 10*((level-1)^2)+100;
            health = (int)(baseHealth+factorHealth*level);
            healthMax = (int)(baseHealth+factorHealth*level);
            mana = (int)(baseMana+factorMana*level);
            manaMax = (int)(baseMana+factorMana*level);
            manaRegen = (int)(baseManaRegen+factorManaRegen*level);
            fortitude = (int)(baseFortitude+factorFortitude*level);
            speed = (int)(baseSpeed+factorSpeed*level);
            Output.write("Player leveled up to level " + level);
            addXP(0); //Incase player levels up more than once
        }
    }

}
