package workexpIT.merlin.entities;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.attacks.Fireball;
import workexpIT.merlin.attacks.Punch;
import workexpIT.merlin.data.EventReader;
import workexpIT.merlin.data.ImageReader;

/**
 * Created by ict11 on 2016-02-10.
 */
public class Gwen extends Entity {

    private boolean overrideAI = false;
    private int overrideAction;

    public Gwen(int x, int y, int state, int level, String dialog, boolean talk) {
        super(x, y, "gwen", state, level, ImageReader.loadImage("resources/graphics/charactersprites/gwen.png"));
        talkable = talk;
        this.dialog = dialog;
        this.attacks[0] = new Punch();
        this.attacks[1] = new Fireball();
        this.health = 100;
        downWalkingSpritesId = new int[]{3, 4, 3, 5};
        upWalkingSpritesId = new int[]{0, 1, 0, 2};
        rightWalkingSpritesId = new int[]{9, 10, 9, 11};
        leftWalkingSpritesId = new int[]{6, 7, 6, 8};
        upSpriteId = 0;
        downSpriteId = 3;
        rightSpriteId = 9;
        leftSpriteId = 6;
        battleSprite = ImageReader.loadImage("resources/graphics/charactersprites/battle/"+getClass().getSimpleName()+".png");
        loadSprites();
    }

    public void overrideMove(int direction) {
        overrideAI = true;
        overrideAction = direction;
    }

    @Override
    public void runAI() {
        if (overrideAI) {
            move(overrideAction);
            overrideAI = false;
            synchronized(Merlin.eventHandler.syncObject) {
                Merlin.eventHandler.condition = true;
                Merlin.eventHandler.syncObject.notify();
            }
        }
        else {
            int action = (int) (Math.random() * 8);
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
                default:
                    //Stand still
                    break;
            }
        }
    }

}