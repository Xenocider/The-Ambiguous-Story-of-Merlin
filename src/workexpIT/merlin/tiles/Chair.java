package workexpIT.merlin.tiles;

import workexpIT.merlin.entities.Entity;

public class Chair extends Tile {

    public Chair() {
        super(1,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }
}
