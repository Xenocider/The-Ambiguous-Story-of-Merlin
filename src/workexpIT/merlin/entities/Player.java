package workexpIT.merlin.entities;

import workexpIT.merlin.graphics.Drawer;

import java.nio.ByteBuffer;

public class Player extends Entity{

    public static ByteBuffer[] sprites = {
            Drawer.loadTexture("resources/graphics/charactersprites/player/0.png")
    };

    public Player(int x, int y, int level) {
        super(x, y, "player", Entity.STATE_NEUTRAL, level, sprites);
    }
}
