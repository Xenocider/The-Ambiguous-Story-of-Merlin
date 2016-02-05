package workexpIT.merlin.tiles;

import workexpIT.merlin.Reference;
import workexpIT.merlin.graphics.Drawer;

public class Grass extends Tile {

    public Grass() {
        super(Reference.grass, Drawer.loadTexture("resources/graphics/materials/"+Reference.grass+".png"));
    }
}
