package workexpIT.merlin.tiles;

import workexpIT.merlin.entities.Entity;

/**
 * Created by ict11 on 2016-04-06.
 */
public class Water extends Tile {

    public Water() {
        super(6);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }
}