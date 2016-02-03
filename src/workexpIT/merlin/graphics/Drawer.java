package workexpIT.merlin.graphics;

/**
 * Created by ict11 on 2016-02-03.
 */

import static org.lwjgl.opengl.GL11.*;


public class Drawer {

    public static void start() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // Resets any previous projection matrices
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        glBegin(GL_QUADS);
        glVertex2i(0,0);
        glVertex2i(100,100);
    }

}
