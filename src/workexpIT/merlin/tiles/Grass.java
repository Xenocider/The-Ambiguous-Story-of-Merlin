package workexpIT.merlin.tiles;

import workexpIT.merlin.Reference;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.graphics.Drawer;

import java.awt.image.BufferedImage;

public class Grass extends Tile {

    public Grass() {
        super(ImageReader.loadImage("resources/graphics/materials/Grass.png"));
    }
}
