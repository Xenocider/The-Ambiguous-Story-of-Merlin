package workexpIT.merlin.entities;

import workexpIT.merlin.graphics.Drawer;

import java.nio.ByteBuffer;

/**
 * Created by ict11 on 2016-02-10.
 */
public class Bob extends Entity {

    public static ByteBuffer[] sprites = {
            Drawer.loadTexture("resources/graphics/charactersprites/player/0.png")
    };

    public Bob(int x, int y, int state, int level) {
        super(x, y, "bob", state, level, sprites);
    }

}
