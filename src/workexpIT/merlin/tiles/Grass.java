package workexpIT.merlin.tiles;

import workexpIT.merlin.data.ImageReader;

public class Grass extends Tile {

    public Grass() {
        super(ImageReader.loadImage("resources/graphics/materials/Grass.png"));
    }
}
