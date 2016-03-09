package workexpIT.merlin.tiles;


import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Output;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Created by ict11 on 2016-02-03.
 */
public class Tile {

    public Tile(BufferedImage texture) {
        this.texture = texture;
    }
    protected BufferedImage texture;

    public enum Rotation {UP,RIGHT,DOWN,LEFT}
    public enum Flip {HORIZONTAL,VERTICAL}
    public Rotation rotation = Rotation.UP;
    public Flip flip = null;

    protected boolean door;
    protected String doorMap;
    protected int doorX;
    protected int doorY;

    public void setDoor(boolean b, String map, int x, int y) {
        door = b;
        doorMap = map;
        doorX = x;
        doorY = y;
    }

    public void setRotation(Rotation rot){
        rotation = rot;
    }
    public void setFlip(Flip f){
        flip = f;
    }


    public boolean movingOnToTile(Entity entity) {
        boolean status = true;
        if (!movingOnToTileExtra(entity)) {
            status = false;
        }
        if (checkForDoor(entity)) {
            status = false;
        }
        return status;
    }

    public boolean movingOnToTileExtra(Entity e) {
        //Used in child classes only
        return true;
    }

    public boolean checkForDoor(Entity entity) {
        if (entity.getClass().equals(Player.class)) {
            if (door) {
                Output.write("DOOOOOOOOR");
                for (int i = 0; i < WorldData.entities.size(); i++) {
                    if (WorldData.entities.get(i).getName() == "player") {
                        WorldData.entities.get(i).setX(doorX);
                        WorldData.entities.get(i).setY(doorY);
                    }
                }
                DataReader.loadMap(doorMap);
                return true;
            }
        }
        return false;
    }
    public BufferedImage getTexture() {
        return texture;
    }
}

