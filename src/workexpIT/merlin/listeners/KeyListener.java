package workexpIT.merlin.listeners;

import org.lwjgl.glfw.GLFWKeyCallback;
import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.graphics.Drawer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;

public class KeyListener extends GLFWKeyCallback {


    public boolean upPressed;
    public boolean rightPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean moveUpTemp;
    public boolean moveRightTemp;
    public boolean moveDownTemp;
    public boolean moveLeftTemp;


    //KeyListener
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (action == GLFW_RELEASE) {
                switch (key) {
                    case GLFW_KEY_UP:
                        upPressed = false;
                        break;
                    case GLFW_KEY_RIGHT:
                        rightPressed = false;
                        break;
                    case GLFW_KEY_LEFT:
                        leftPressed = false;
                        break;
                    case GLFW_KEY_DOWN:
                        downPressed = false;
                        break;
                }
                    return;
            }

            switch (key) {
                case GLFW_KEY_ESCAPE:
                    glfwSetWindowShouldClose(window, GLFW_TRUE);
                    break;
                case GLFW_KEY_UP:
                    upPressed = true;
                    rightPressed = false;
                    downPressed = false;
                    leftPressed = false;
                    moveUpTemp = true;
                    moveRightTemp = false;
                    moveDownTemp = false;
                    moveLeftTemp = false;
                    break;
                case GLFW_KEY_RIGHT:
                    upPressed = false;
                    rightPressed = true;
                    downPressed = false;
                    leftPressed = false;
                    moveUpTemp = false;
                    moveRightTemp = true;
                    moveDownTemp = false;
                    moveLeftTemp = false;
                    break;
                case GLFW_KEY_LEFT:
                    upPressed = false;
                    rightPressed = false;
                    downPressed = false;
                    leftPressed = true;
                    moveUpTemp = false;
                    moveRightTemp = false;
                    moveDownTemp = false;
                    moveLeftTemp = true;
                    break;
                case GLFW_KEY_DOWN:
                    upPressed = false;
                    rightPressed = false;
                    downPressed = true;
                    leftPressed = false;
                    moveUpTemp = false;
                    moveRightTemp = false;
                    moveDownTemp = true;
                    moveLeftTemp = false;
                    break;
            }
        }

}
