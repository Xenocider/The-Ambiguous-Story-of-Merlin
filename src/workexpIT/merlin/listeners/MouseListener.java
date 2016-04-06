package workexpIT.merlin.listeners;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.tiles.Tile;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Created by ict11 on 2016-02-24.
 */
public class MouseListener implements java.awt.event.MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();
        Output.write("MOUSELOC CLICK = " + x +" " + y);
        if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            if (x > JavaDrawer.frame.getWidth()-JavaDrawer.editorMenuSize && x < JavaDrawer.frame.getWidth()-10-32) {
                int editX = (x - JavaDrawer.frame.getWidth() + JavaDrawer.editorMenuSize - 10)/(JavaDrawer.imageSize*4+10);
                int editY = (y - 10)/(JavaDrawer.imageSize*4+10);
                int id = editX + editY*2;
                if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
                    //If it is the right mouse button
                    int instance = WorldData.menuTiles.get(id).instance+1;
                    if (instance > WorldData.menuTiles.get(id).maxInstances-1) {
                        WorldData.menuTiles.get(id).setInstance(0);
                    }
                    else {
                        WorldData.menuTiles.get(id).setInstance(instance);
                    }

                }
                WorldData.selectedTile = id;
                WorldData.selectedInstance = WorldData.menuTiles.get(id).instance;
                Output.write("Selected tile: " + id);
            }
            else if (x>JavaDrawer.frame.getWidth()-10-32 && y < 60) {
                DataReader.saveMap(WorldData.mapName);
            }
            else if (x<JavaDrawer.frame.getWidth()-JavaDrawer.editorMenuSize){
                //int mapX = (int)((x/JavaDrawer.scale - JavaDrawer.offsetX)/JavaDrawer.imageSize);
                //int mapY = (int)((y/JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize)/JavaDrawer.imageSize);
                int mapX = (int)((x-JavaDrawer.frame.getWidth()/2)/JavaDrawer.scale - JavaDrawer.offsetX)/JavaDrawer.imageSize;
                int mapY = (int)((y-JavaDrawer.frame.getHeight()/2)/JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize)/JavaDrawer.imageSize;
                Output.write(mapX + ", " + mapY);
                try {
                    Tile tile = (Tile) Class.forName("workexpIT.merlin.tiles."+ Reference.tileIds[WorldData.selectedTile]).newInstance();
                    tile.setInstance(WorldData.selectedInstance);
                    WorldData.tiles[mapX][mapY] = tile;
                    Output.write("Placing tile " + WorldData.selectedTile + " at " + mapX + ", " + mapY);
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        }
        else if (Merlin.mode.equals(Merlin.Mode.BATTLE)) {
            if (!GameLoop.fightMenu) {
                if (x > JavaDrawer.fightX && x < JavaDrawer.fightX + JavaDrawer.fightButtonWidth && y > JavaDrawer.fightY && y < JavaDrawer.fightY + JavaDrawer.fightButtonHeight) {
                    Output.write("FIGHT!");
                    GameLoop.fightMenu = true;
                } else if (x > JavaDrawer.bagX && x < JavaDrawer.bagX + JavaDrawer.otherButtonWidth && y > JavaDrawer.bagY && y < JavaDrawer.bagY + JavaDrawer.otherButtonHeight) {
                    Output.write("BAG!");
                } else if (x > JavaDrawer.restX && x < JavaDrawer.restX + JavaDrawer.otherButtonWidth && y > JavaDrawer.restY && y < JavaDrawer.restY + JavaDrawer.otherButtonHeight) {
                    Output.write("REST!");
                }
            }
            else {
                if (x > JavaDrawer.attack1X && x < JavaDrawer.attack1X + JavaDrawer.attackButtonWidth && y > JavaDrawer.attack1Y && y < JavaDrawer.attack1Y + JavaDrawer.attackButtonHeight) {
                    GameLoop.currentAttack = WorldData.getPlayer().attacks[0];
                }
                if (x > JavaDrawer.attack2X && x < JavaDrawer.attack2X + JavaDrawer.attackButtonWidth && y > JavaDrawer.attack2Y && y < JavaDrawer.attack2Y + JavaDrawer.attackButtonHeight) {
                    GameLoop.currentAttack = WorldData.getPlayer().attacks[1];

                }
                if (x > JavaDrawer.attack3X && x < JavaDrawer.attack3X + JavaDrawer.attackButtonWidth && y > JavaDrawer.attack3Y && y < JavaDrawer.attack3Y + JavaDrawer.attackButtonHeight) {
                    GameLoop.currentAttack = WorldData.getPlayer().attacks[2];

                }
                if (x > JavaDrawer.attack4X && x < JavaDrawer.attack4X + JavaDrawer.attackButtonWidth && y > JavaDrawer.attack4Y && y < JavaDrawer.attack4Y + JavaDrawer.attackButtonHeight) {
                    GameLoop.currentAttack = WorldData.getPlayer().attacks[3];

                }
                if (x > JavaDrawer.attack5X && x < JavaDrawer.attack5X + JavaDrawer.attackButtonWidth && y > JavaDrawer.attack5Y && y < JavaDrawer.attack5Y + JavaDrawer.attackButtonHeight) {
                    GameLoop.currentAttack = WorldData.getPlayer().attacks[4];
                }
                if (x > JavaDrawer.attack6X && x < JavaDrawer.attack6X + JavaDrawer.attackButtonWidth && y > JavaDrawer.attack6Y && y < JavaDrawer.attack6Y + JavaDrawer.attackButtonHeight) {
                    GameLoop.currentAttack = WorldData.getPlayer().attacks[5];

                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
