package workexpIT.merlin.entities;

import workexpIT.merlin.graphics.Drawer;

import java.nio.ByteBuffer;

/**
 * Created by ict11 on 2016-02-10.
 */
public class George extends Entity {

    public static ByteBuffer[] sprites = {
            Drawer.loadTexture("resources/graphics/charactersprites/george/0.png")
    };

    public George(int x, int y, int state, int level) {
        super(x, y, "george", state, level, sprites);
    }

    @Override
    public void runAI() {
        int action = (int) (Math.random()*4);
        switch (action) {
            case Entity.MOVE_UP:
                move(Entity.MOVE_UP);
                break;
            case Entity.MOVE_RIGHT:
                move(Entity.MOVE_RIGHT);
                break;
            case Entity.MOVE_DOWN:
                move(Entity.MOVE_DOWN);
                break;
            case Entity.MOVE_LEFT:
                move(Entity.MOVE_LEFT);
                break;
            case 4:
                //Stand still
                break;
        }
    }

}