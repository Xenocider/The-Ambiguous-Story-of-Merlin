package workexpIT.merlin.tiles;

import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.Drawer;

public class WoodFence extends Tile {

    public WoodFence() {
        super(5,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }
}