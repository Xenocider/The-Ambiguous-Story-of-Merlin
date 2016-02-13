package workexpIT.merlin.tiles;


import workexpIT.merlin.Output;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;

import java.nio.ByteBuffer;

/**
 * Created by ict11 on 2016-02-03.
 */
public class Tile {

    public Tile(ByteBuffer texture) {
        this.texture = texture;
    }
    public ByteBuffer texture;

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

    public boolean movingOnToTile(Entity entity) {
        return checkForDoor(entity);
    }

    public boolean checkForDoor(Entity entity) {
        if (entity.getClass().equals(Player.class)) {
            if (door) {
                Output.write("DOOOOOOOOR");
                DataReader.loadMap(doorMap);
                for (int i = 0; i < WorldData.entities.size(); i++) {
                    if (WorldData.entities.get(i).getName() == "player") {
                        WorldData.entities.get(i).setX(doorX);
                        WorldData.entities.get(i).setY(doorY);
                    }
                }
                return false;
            }
        }
        return true;
    }
}

