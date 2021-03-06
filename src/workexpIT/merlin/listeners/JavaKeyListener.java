package workexpIT.merlin.listeners;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.tiles.Tile;

import javax.swing.*;
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
            case KeyEvent.VK_CLOSE_BRACKET:
                //Flip Horizontally
                if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
                    System.out.println("Flipping the " + WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].getClass().getName() + " tile at " + getTileCoordsAtCursor()[0] + " " + getTileCoordsAtCursor()[1]);
                    switch (WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].flip) {
                        case DEFAULT:
                            WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setFlip(Tile.Flip.HORIZONTAL);
                            break;
                        case HORIZONTAL:
                            WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setFlip(Tile.Flip.DEFAULT);
                            break;
                    }
                    JavaDrawer.redrawMap(getTileCoordsAtCursor());
                }

                break;
            case KeyEvent.VK_OPEN_BRACKET:
                //Flip Vertically
                if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {

                    Output.write("FLIP " + WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].flip);
                switch (WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].flip) {
                    case DEFAULT:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setFlip(Tile.Flip.VERTICAL);
                        break;
                    case VERTICAL:
                        WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].setFlip(Tile.Flip.DEFAULT);
                        break;
                }
                JavaDrawer.redrawMap(getTileCoordsAtCursor());
        }
                break;
            case KeyEvent.VK_COMMA:
                //Rotate Left
                if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {

                    if (WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].flip == Tile.Flip.DEFAULT) {
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
                }
                else {
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
                }
        JavaDrawer.redrawMap(getTileCoordsAtCursor());
    }
                break;
            case KeyEvent.VK_PERIOD:
                //Rotate Right
                if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {

                    Output.write("Rotating tile at " + getTileCoordsAtCursor()[0] + ", " + getTileCoordsAtCursor()[1]);
                Output.write("Title " + WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]]);
                if (WorldData.tiles[getTileCoordsAtCursor()[0]][getTileCoordsAtCursor()[1]].flip == Tile.Flip.DEFAULT) {
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
                }
                else {
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
                }
    JavaDrawer.redrawMap(getTileCoordsAtCursor());
}
                break;
            case KeyEvent.VK_E:
                GameLoop.switchToEntityEditor();
                break;
            case KeyEvent.VK_R:

                Thread door = new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        DataReader.loadMap(WorldData.mapName);
                        Output.write("DOOOOOOOOR");
                        WorldData.getPlayer().setX(WorldData.enterX);
                        WorldData.getPlayer().setY(WorldData.enterY);
                        WorldData.getPlayer().lastLoc[0] = WorldData.enterX;
                        WorldData.getPlayer().lastLoc[1] = WorldData.enterY;
                        JavaDrawer.offsetX = (-WorldData.getPlayer().getX() * JavaDrawer.imageSize - WorldData.getPlayer().downSprite.getWidth() / JavaDrawer.scale);
                        JavaDrawer.offsetY = (-WorldData.getPlayer().getY() * JavaDrawer.imageSize + WorldData.getPlayer().downSprite.getHeight() / JavaDrawer.scale);
                    }
                };
                JavaDrawer.fadeAway = true;
                door.start();
                break;
        }

        if (JavaDrawer.drawDialog && JavaDrawer.pause) {
            Output.write("End display");
            JavaDrawer.line = 0;
            JavaDrawer.lines.clear();
            JavaDrawer.textCount = 0;
            GameLoop.dialogText = null;
            JavaDrawer.drawDialog = false;
            GameLoop.pause = false;
            JavaDrawer.pause = false;
            synchronized(Merlin.eventHandler.syncObject) {
                Merlin.eventHandler.condition = true;
                Merlin.eventHandler.syncObject.notify();
            }
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
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation,JavaDrawer.frame);
        double x = mouseLocation.getX();
        double y = mouseLocation.getY();
        //y = y - 21; //not sure why it is off
        Output.write("MOUSELOC = " + x +" " + y);
        int mapX = -1;
        int mapY = -1;
        if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            if (x < JavaDrawer.frame.getWidth() - JavaDrawer.editorMenuSize) {
                mapX = (int) (((x - JavaDrawer.frame.getWidth() / 2) / JavaDrawer.scale - JavaDrawer.offsetX) / JavaDrawer.imageSize-0.5);
                mapY = (int) ((y - JavaDrawer.frame.getHeight() / 2) / JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize) / JavaDrawer.imageSize-2;
            }
        }
        Output.write(mapX + "   " + mapY);
        return new int[] {mapX,mapY};
    }
}
