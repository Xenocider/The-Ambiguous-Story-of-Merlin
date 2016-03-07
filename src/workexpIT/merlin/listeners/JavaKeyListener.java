package workexpIT.merlin.listeners;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.graphics.JavaDrawer;

import java.awt.event.*;

/**
 * Created by ict11 on 2016-02-22.
 */
public class JavaKeyListener implements java.awt.event.KeyListener {

    public boolean upPressed;
    public boolean rightPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean upTemp;
    public boolean rightTemp;
    public boolean downTemp;
    public boolean leftTemp;


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                rightPressed = false;
                downPressed = false;
                leftPressed = false;
                upTemp = true;
                rightTemp = false;
                downTemp = false;
                leftTemp = false;
                break;
            case KeyEvent.VK_RIGHT:
                upPressed = false;
                rightPressed = true;
                downPressed = false;
                leftPressed = false;
                upTemp = false;
                rightTemp = true;
                downTemp = false;
                leftTemp = false;
                break;
            case KeyEvent.VK_LEFT:
                upPressed = false;
                rightPressed = false;
                downPressed = false;
                leftPressed = true;
                upTemp = false;
                rightTemp = false;
                downTemp = false;
                leftTemp = true;
                break;
            case KeyEvent.VK_DOWN:
                upPressed = false;
                rightPressed = false;
                downPressed = true;
                leftPressed = false;
                upTemp = false;
                rightTemp = false;
                downTemp = true;
                leftTemp = false;
                break;
            case KeyEvent.VK_EQUALS:
                if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
                    JavaDrawer.scaleUp();
                }
                break;
            case KeyEvent.VK_MINUS:
                if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
                    JavaDrawer.scaleDown();
                }
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
        }

    }
}
