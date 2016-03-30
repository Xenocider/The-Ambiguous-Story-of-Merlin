package workexpIT.merlin.listeners;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.tiles.Tile;

import java.awt.*;
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
            case KeyEvent.VK_BRACERIGHT:
                //Flip Horizontally
                WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setFlip(Tile.Flip.HORIZONTAL);

                break;
            case KeyEvent.VK_BRACELEFT:
                //Flip Vertically
                WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setFlip(Tile.Flip.VERTICAL);
                break;
            case KeyEvent.VK_COMMA:
                //Rotate Left
                switch (WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].rotation) {
                    case UP:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.LEFT);
                        break;
                    case RIGHT:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.UP);
                        break;
                    case DOWN:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.RIGHT);
                        break;
                    case LEFT:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.DOWN);
                        break;
                }
                break;
            case KeyEvent.VK_PERIOD:
                //Rotate Right
                switch (WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].rotation) {
                    case UP:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.RIGHT);
                        break;
                    case RIGHT:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.DOWN);
                        break;
                    case DOWN:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.LEFT);
                        break;
                    case LEFT:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setRotation(Tile.Rotation.UP);
                        break;
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

    public int[] getTileCoordsAtCursor() {
        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getX();
        int mapX = -1;
        int mapY = -1;
        if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            if (x < JavaDrawer.frame.getWidth() - JavaDrawer.editorMenuSize) {
                mapX = (int) ((x - JavaDrawer.frame.getWidth() / 2) / JavaDrawer.scale - JavaDrawer.offsetX) / JavaDrawer.imageSize;
                mapY = (int) ((y - JavaDrawer.frame.getHeight() / 2) / JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize) / JavaDrawer.imageSize;
            }
        }
        Output.write(mapX + "   " + mapY);
        return new int[] {mapX,mapY};
    }
}
