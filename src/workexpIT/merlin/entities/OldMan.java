package workexpIT.merlin.entities;

import workexpIT.merlin.attacks.Fireball;
import workexpIT.merlin.attacks.Punch;
import workexpIT.merlin.data.ImageReader;

/**
 * Created by ict11 on 2016-02-10.
 */
public class OldMan extends Entity {

    public OldMan(int x, int y, int state, int level, String dialog, boolean talk) {
        super(x, y, "oldman", state, level, ImageReader.loadImage("resources/graphics/charactersprites/oldman.png"));
        talkable = talk;
        this.dialog = dialog;
        this.attacks[0] = new Punch();
        this.health = 100;
        downWalkingSpritesId = new int[]{11, 0, 11, 1};
        upWalkingSpritesId = new int[]{5, 3, 5, 4};
        rightWalkingSpritesId = new int[]{8, 6, 8, 7};
        leftWalkingSpritesId = new int[]{2, 9, 2, 10};
        upSpriteId = 5;
        downSpriteId = 11;
        rightSpriteId = 8;
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