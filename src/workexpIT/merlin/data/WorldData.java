package workexpIT.merlin.data;

import workexpIT.merlin.Reference;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ict11 on 2016-02-03.
 */
public class WorldData {

        public static Tile[][] tiles = new Tile[Reference.mapSize][Reference.mapSize];
        public static List<Entity> entities = new ArrayList<Entity>();
}
