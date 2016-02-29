package workexpIT.merlin.listeners;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.tiles.Tile;

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
        if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            if (x > JavaDrawer.frame.getWidth()-JavaDrawer.editorMenuSize && x < JavaDrawer.frame.getWidth()-10-32) {
                int editX = (x - JavaDrawer.frame.getWidth() + JavaDrawer.editorMenuSize - 10)/(JavaDrawer.imageSize*4+10);
                int editY = (y - 10)/(JavaDrawer.imageSize*4+10);
                int id = editX + editY*2;
                WorldData.selectedTile = id;
                Output.write("Selected tile: " + id);
            }
            else if (x>JavaDrawer.frame.getWidth()-10-32 && y < 60) {
                Output.write("Saving map...");
                DataReader.saveMap(WorldData.mapName);
            }
            else if (x<JavaDrawer.frame.getWidth()-JavaDrawer.editorMenuSize){
                int mapX = (int)((x/JavaDrawer.scale - JavaDrawer.offsetX)/JavaDrawer.imageSize);
                int mapY = (int)((y/JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize)/JavaDrawer.imageSize);
                try {
                    WorldData.tiles[mapX][mapY] = (Tile) Class.forName("workexpIT.merlin.tiles."+ Reference.tileIds[WorldData.selectedTile]).newInstance();
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
