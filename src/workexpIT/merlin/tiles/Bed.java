package workexpIT.merlin.tiles;

import workexpIT.merlin.entities.Entity;

public class Bed extends Tile {

    public Bed() {
        super(2,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }
}
