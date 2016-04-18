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


    public Player(int x, int y, int level) {
        super(x, y, "player", Entity.STATE_NEUTRAL, level, ImageReader.loadImage("resources/graphics/charactersprites/player.png"));
        attacks[0] = new Punch();
        attacks[1] = new Fireball();
        attacks[2] = new DrainLife();
        attacks[3] = new DrainMana();
        attacks[4] = new Flood();
        attacks[5] = new LifeBend();
        downWalkingSpritesId = new int[]{4, 3, 5, 3};
        upWalkingSpritesId = new int[]{1, 0, 2, 0};
        rightWalkingSpritesId = new int[]{10, 9, 11, 9};
        leftWalkingSpritesId = new int[]{7, 6, 8, 6};
        upSpriteId = 0;
        downSpriteId = 3;
        rightSpriteId = 9;
        leftSpriteId = 6;
        battleSprite = ImageReader.loadImage("resources/graphics/charactersprites/battle/"+getClass().getSimpleName()+".png");
        loadSprites();
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
