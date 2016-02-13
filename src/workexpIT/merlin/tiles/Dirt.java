package workexpIT.merlin.tiles;

import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.Drawer;

public class Dirt extends Tile {

    public Dirt() {
        super(Drawer.loadTexture("resources/graphics/materials/Dirt.png"));
    }

    @Override
    public boolean movingOnToTile(Entity e) {
        checkForDoor(e);
        return false;
    }
}