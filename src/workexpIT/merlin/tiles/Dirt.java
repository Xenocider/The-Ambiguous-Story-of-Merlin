package workexpIT.merlin.tiles;

import workexpIT.merlin.Reference;
import workexpIT.merlin.graphics.Drawer;

public class Dirt extends Tile {

    public Dirt() {
        super(Reference.dirt, Drawer.loadTexture("resources/graphics/materials/" + Reference.dirt + ".png"));
    }

    @Override
    public boolean movingOnToTile() {
        return false;
    }
}