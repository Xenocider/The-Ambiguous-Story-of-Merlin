package workexpIT.merlin.listeners;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Bob;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.tiles.Tile;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ict11 on 2016-02-24.
 */
public class MouseListener implements java.awt.event.MouseListener {

    public static boolean pressed = false;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        pressed = true;

        int x = e.getX();
        int y = e.getY();
        Output.write("MOUSELOC CLICK = " + x +" " + y);

        if (Merlin.mode.equals(Merlin.Mode.EDITOR) && GameLoop.tileEditor) {
            if (x > JavaDrawer.frame.getWidth() - JavaDrawer.editorMenuSize && x < JavaDrawer.frame.getWidth() - 10 - 32) {
                int editX = (x - JavaDrawer.frame.getWidth() + JavaDrawer.editorMenuSize - 10) / (JavaDrawer.imageSize * 4 + 10);
                int editY = (y - 10) / (JavaDrawer.imageSize * 4 + 10);
                int id = editX + editY * 2;
                if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
                    //If it is the right mouse button
                    int instance = WorldData.menuTiles.get(id).instance + 1;
                    if (instance > WorldData.menuTiles.get(id).maxInstances - 1) {
                        WorldData.menuTiles.get(id).setInstance(0);
                    } else {
                        WorldData.menuTiles.get(id).setInstance(instance);
                    }

                }
                WorldData.selectedTile = id;
                WorldData.selectedInstance = WorldData.menuTiles.get(id).instance;
                Output.write("Selected tile: " + id);
            } else if (x > JavaDrawer.frame.getWidth() - 10 - 32 && y < 60) {
                DataReader.saveMap(WorldData.mapName);
            } else if (x < JavaDrawer.frame.getWidth() - JavaDrawer.editorMenuSize) {
                //int mapX = (int)((x/JavaDrawer.scale - JavaDrawer.offsetX)/JavaDrawer.imageSize);
                //int mapY = (int)((y/JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize)/JavaDrawer.imageSize);
                int mapX = (int) ((x - JavaDrawer.frame.getWidth() / 2) / JavaDrawer.scale - JavaDrawer.offsetX) / JavaDrawer.imageSize;
                int mapY = (int) ((y - JavaDrawer.frame.getHeight() / 2) / JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize) / JavaDrawer.imageSize;
                Output.write(mapX + ", " + mapY);
                placeTile(WorldData.selectedTile, WorldData.selectedInstance, mapX, mapY);
            }
        }
            else if (Merlin.mode.equals(Merlin.Mode.EDITOR) && !GameLoop.tileEditor) {
                if (x > JavaDrawer.frame.getWidth()-JavaDrawer.editorMenuSize && x < JavaDrawer.frame.getWidth()-10-32) {
                    int editX = (x - JavaDrawer.frame.getWidth() + JavaDrawer.editorMenuSize - 10)/(JavaDrawer.imageSize*4+10);
                    int editY = (y - 10)/(JavaDrawer.imageSize*4+10);
                    int id = editX + editY*2;
                    WorldData.selectedEntity = id;
                    Output.write("Selected entity: " + id);
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
                    placeEntity(WorldData.selectedEntity,mapX,mapY);
                }
        }
        else if (Merlin.mode.equals(Merlin.Mode.BATTLE)) {
                if (x > JavaDrawer.fightX && x < JavaDrawer.fightX + GameLoop.buttonAttack.getWidth() && y > JavaDrawer.fightY && y < JavaDrawer.fightY + GameLoop.buttonAttack.getHeight()) {
                    Output.write("FIGHT!");
                    GameLoop.fightMenu = true;
                } else if (x > JavaDrawer.bagX && x < JavaDrawer.bagX + GameLoop.buttonBag.getWidth() && y > JavaDrawer.bagY && y < JavaDrawer.bagY + GameLoop.buttonBag.getHeight()) {
                    Output.write("BAG!");
                } else if (x > JavaDrawer.restX && x < JavaDrawer.restX + GameLoop.buttonDefend.getWidth() && y > JavaDrawer.restY && y < JavaDrawer.restY + GameLoop.buttonDefend.getHeight()) {
                    Output.write("REST!");
                } else if (x > JavaDrawer.characterX && x < JavaDrawer.characterX + GameLoop.buttonCharacter.getWidth() && y > JavaDrawer.characterY && y < JavaDrawer.characterY + GameLoop.buttonCharacter.getHeight()) {
                    Output.write("CHARACTER!");
                }

            if (GameLoop.fightMenu) {
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
        if (Merlin.mode.equals(Merlin.Mode.MENU)) {
            if (y > 300 && y < 380) {
                Merlin.startGame();
                pressed = false;
            }
            else if (y > 490 && y < 580) {
                Merlin.startEditor();
                pressed = false;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void placeEntity(int selectedEntity, int mapX, int mapY) {
        boolean go = true;
        for (int i = 0; i < WorldData.entities.size(); i ++) {
            for (int checkX = 0; checkX < WorldData.entities.get(i).entityWidth/16 && go; checkX++) {
                for (int checkY = 0; checkY < WorldData.entities.get(i).entityHeight/16 && go; checkY++) {
                    if ((WorldData.entities.get(i).getX()+checkX) == mapX && (WorldData.entities.get(i).getY()+checkY) == mapY) {
                        go = false;
                        WorldData.entities.remove(i);
                    }
                }
            }
        }
        Entity entity = null;
        try {
            Class<?> clazz = Class.forName("workexpIT.merlin.entities."+ Reference.entities[selectedEntity]);
            Constructor<?> ctor = null;
            try {
                ctor = clazz.getConstructor(int.class,int.class,int.class,int.class,String.class,boolean.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            entity = (Entity) ctor.newInstance(mapX,mapY,0,1,null,false);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        WorldData.entities.add(entity);
    }

    public static void placeTile(int id, int inst, int x, int y) {
        Tile tile = null;
        try {
            tile = (Tile) Class.forName("workexpIT.merlin.tiles."+ Reference.tileIds[id]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tile.setInstance(WorldData.selectedInstance);
        WorldData.tiles[x][y] = tile;
        Output.write("Placing tile " + id + " at " + x + ", " + y);
        JavaDrawer.redrawMap(new int[]{x,y});
    }
}
