package workexpIT.merlin.tiles;

import workexpIT.merlin.entities.Entity;

/**
 * Created by ict11 on 2016-04-06.
 */
public class Water extends Tile {

    public Water() {
        super(10,true);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }

    @Override
    public void changeAnimationStage() {
        if ((int) (Math.random() * 20) == 0 || animationStage > 0) {
            animationStage = animationStage + 1;
            if (animationStage == maxAnimationStage) {
                animationStage = 0;
            }
        }
    }
}
