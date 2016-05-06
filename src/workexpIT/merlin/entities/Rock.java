package workexpIT.merlin.entities;

import workexpIT.merlin.Output;
import workexpIT.merlin.attacks.Fireball;
import workexpIT.merlin.attacks.Punch;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.SoundHandler;
import workexpIT.merlin.data.WorldData;

/**
 * Created by ict11 on 2016-02-10.
 */
public class Rock extends Entity {

    private boolean fall;
    public int fallDis;

    public Rock(int x, int y, int state, int level, String dialog, boolean talk) {

        super(x, y, "rock", state, level, ImageReader.loadImage("resources/graphics/charactersprites/rock.png"));
        this.dialog = dialog;
        talkable = talk;

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
            SoundHandler.playNewSound("resources/audio/slide.wav", 5);
        }
        else if (pX < x) {
            move(MOVE_RIGHT);
            SoundHandler.playNewSound("resources/audio/slide.wav", 5);
        }
        if (pY > y) {
            move(MOVE_UP);
            SoundHandler.playNewSound("resources/audio/slide.wav", 5);
        }
        else if (pY < y) {
            move(MOVE_DOWN);
            SoundHandler.playNewSound("resources/audio/slide.wav", 5);
        }
    }

    public void fall(int distance) {
        Output.write("recieved");
        fall = true;
        fallDis = distance;
    }

    @Override
    public void runAI() {
        if (fall) {
            moveOverride(MOVE_DOWN);
            fallDis = fallDis - 1;
            if (fallDis == 0) {
                fall = false;
            }
        }
    }
}