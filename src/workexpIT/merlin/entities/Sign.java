package workexpIT.merlin.entities;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.data.ImageReader;

/**
 * Created by ict11 on 2016-02-10.
 */
public class Sign extends Entity {

    public String text = "No Text Has Been Set Yet";

    public Sign(int x, int y, int state, int level, String dialog) {

        super(x, y, "sign", state, level, ImageReader.loadImage("resources/graphics/charactersprites/sign.png"));
        this.dialog = dialog;

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

    public void setText(String s) {
        text = s;
    }

}