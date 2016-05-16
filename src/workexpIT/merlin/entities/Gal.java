package workexpIT.merlin.entities;

import workexpIT.merlin.attacks.Punch;
import workexpIT.merlin.data.ImageReader;

/**
 * Created by ict11 on 2016-02-10.
 */
public class Gal extends Entity {

    public Gal(int x, int y, int state, int level, String dialog, boolean talk) {
        super(x, y, "gal", state, level, ImageReader.loadImage("resources/graphics/charactersprites/gal.png"));
        talkable = talk;
        this.dialog = dialog;
        this.attacks[0] = new Punch();
        this.health = 100;
        downWalkingSpritesId = new int[]{0, 3, 0, 4};
        upWalkingSpritesId = new int[]{1, 5, 1, 6};
        rightWalkingSpritesId = new int[]{9, 10, 9, 11};
        leftWalkingSpritesId = new int[]{2, 7, 2, 8};
        upSpriteId = 1;
        downSpriteId = 0;
        rightSpriteId = 9;
        leftSpriteId = 2;
        battleSprite = ImageReader.loadImage("resources/graphics/charactersprites/battle/"+getClass().getSimpleName()+".png");
        loadSprites();
    }

    @Override
    public void runAI() {
        int action = (int) (Math.random()*4);
        switch (action) {
            case Entity.MOVE_UP:
                move(Entity.MOVE_UP);
                break;
            case Entity.MOVE_RIGHT:
                move(Entity.MOVE_RIGHT);
                break;
            case Entity.MOVE_DOWN:
                move(Entity.MOVE_DOWN);
                break;
            case Entity.MOVE_LEFT:
                move(Entity.MOVE_LEFT);
                break;
            case 4:
                //Stand still
                break;
        }
    }

}