package workexpIT.merlin.entities;

import workexpIT.merlin.attacks.Fireball;
import workexpIT.merlin.attacks.Punch;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;

/**
 * Created by ict11 on 2016-02-10.
 */
public class Rock extends Entity {

    public Rock(int x, int y, int state, int level) {

        super(x, y, "rock", state, level, ImageReader.loadImage("resources/graphics/charactersprites/rock.png"));
        downWalkingSpritesId = new int[]{0};
        upWalkingSpritesId = new int[]{0};
        rightWalkingSpritesId = new int[]{0};
        leftWalkingSpritesId = new int[]{0};
        upSpriteId = 0;
        downSpriteId = 0;
        rightSpriteId = 0;
        leftSpriteId = 0;
        maxAnimationStage = 0;
        loadSprites();
    }

    @Override
    public void playerInteraction() {
        int pX = WorldData.getPlayer().getX();
        int pY = WorldData.getPlayer().getY();
        if (pX > x) {
            move(MOVE_LEFT);
        }
        else if (pX < x) {
            move(MOVE_RIGHT);
        }
        if (pY > y) {
            move(MOVE_UP);
        }
        else if (pY < y) {
            move(MOVE_DOWN);
        }
    }
}