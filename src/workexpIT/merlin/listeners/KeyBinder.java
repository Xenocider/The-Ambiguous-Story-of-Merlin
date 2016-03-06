package workexpIT.merlin.listeners;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by justi on 2016-03-05.
 */
public class KeyBinder {

    public enum Pressed {UP,DOWN,RIGHT,LEFT}
    public enum Released {UP,DOWN,RIGHT,LEFT}

    public static boolean upPressed;
    public static boolean rightPressed;
    public static boolean downPressed;
    public static boolean leftPressed;
    public static boolean upTemp;
    public static boolean rightTemp;
    public static boolean downTemp;
    public static boolean leftTemp;

    public static void  init() {
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0,false), "upPressed");
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0,true), "upReleased");
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0,false), "downPressed");
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0,true), "downReleased");
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,false), "rightPressed");
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true), "rightReleased");
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,false), "leftPressed");
        Merlin.jDrawer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,true), "leftReleased");

        Merlin.jDrawer.getActionMap().put("upPressed", new KeyAction(Pressed.UP));
        Merlin.jDrawer.getActionMap().put("upReleased", new KeyAction(Released.UP));
        Merlin.jDrawer.getActionMap().put("downPressed", new KeyAction(Pressed.DOWN));
        Merlin.jDrawer.getActionMap().put("downReleased", new KeyAction(Released.DOWN));
        Merlin.jDrawer.getActionMap().put("rightPressed", new KeyAction(Pressed.RIGHT));
        Merlin.jDrawer.getActionMap().put("rightReleased", new KeyAction(Released.RIGHT));
        Merlin.jDrawer.getActionMap().put("leftPressed", new KeyAction(Pressed.LEFT));
        Merlin.jDrawer.getActionMap().put("leftReleased", new KeyAction(Released.LEFT));
        Output.write("Keybindings loaded");
    }

    public static class KeyAction extends AbstractAction {

        KeyAction(Pressed key) {
            Output.write(key.toString());
            switch (key) {
                case UP:
                    upPressed = true;
                    rightPressed = false;
                    downPressed = false;
                    leftPressed = false;
                    upTemp = true;
                    rightTemp = false;
                    downTemp = false;
                    leftTemp = false;
                    break;
                case RIGHT:
                    upPressed = false;
                    rightPressed = true;
                    downPressed = false;
                    leftPressed = false;
                    upTemp = false;
                    rightTemp = true;
                    downTemp = false;
                    leftTemp = false;
                    break;
                case LEFT:
                    upPressed = false;
                    rightPressed = false;
                    downPressed = false;
                    leftPressed = true;
                    upTemp = false;
                    rightTemp = false;
                    downTemp = false;
                    leftTemp = true;
                    break;
                case DOWN:
                    upPressed = false;
                    rightPressed = false;
                    downPressed = true;
                    leftPressed = false;
                    upTemp = false;
                    rightTemp = false;
                    downTemp = true;
                    leftTemp = false;
                    break;
            }
        }
        KeyAction(Released key) {
            switch (key) {
                case UP:
                    upPressed = false;
                    break;
                case DOWN:
                    downPressed=false;
                    break;
                case RIGHT:
                    rightPressed=false;
                    break;
                case LEFT:
                    leftPressed=false;
                    break;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Output.write("GOGOG");
        }
    }
}
