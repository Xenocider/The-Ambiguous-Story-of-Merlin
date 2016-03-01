package workexpIT.merlin.tiles;

import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.Drawer;

public class Dirt extends Tile {

    public Dirt() {
        super(ImageReader.loadImage("resources/graphics/materials/Dirt.png"));
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        checkForDoor(e);
        return false;
    }
}