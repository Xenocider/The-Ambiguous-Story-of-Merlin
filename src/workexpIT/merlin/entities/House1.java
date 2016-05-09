package workexpIT.merlin.entities;

import workexpIT.merlin.data.ImageReader;

/**
 * Created by ict11 on 2016-02-10.
 */
public class House1 extends Entity {

    public House1(int x, int y, int state, int level, String dialog, boolean talk) {

        super(x, y, "house1", state, level, ImageReader.loadImage("resources/graphics/charactersprites/house1.png"));
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
}