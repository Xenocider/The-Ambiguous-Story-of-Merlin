package workexpIT.merlin.entities;

import workexpIT.merlin.attacks.Fireball;
import workexpIT.merlin.attacks.Punch;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.graphics.Drawer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Player extends Entity{

    public static BufferedImage[] sprites = {
            ImageReader.loadImage("resources/graphics/charactersprites/player/0.png")
    };

    public Player(int x, int y, int level) {
        super(x, y, "player", Entity.STATE_NEUTRAL, level, sprites);
        this.attacks[0] = new Punch();
        this.attacks[1] = new Fireball();
    }
}
