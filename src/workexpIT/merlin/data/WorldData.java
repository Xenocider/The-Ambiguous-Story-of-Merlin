package workexpIT.merlin.data;

import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;
import workexpIT.merlin.tiles.Tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ict11 on 2016-02-03.
 */
public class WorldData {

    public static List<int[]>animatedTiles = new ArrayList<>();

    public static int enterX = 12;
    public static int enterY = 2;

    public static int selectedTile;
    public static List<Tile> menuTiles = new ArrayList<>();

    public static BufferedImage battleBackground;

    public static BufferedImage map;
    public static BufferedImage scaledMap;


    public static String mapName;

    public static int mapSizeX = 50;
    public static int mapSizeY = 50;

    public static Tile[][] tiles = new Tile[WorldData.mapSizeX][WorldData.mapSizeY];
    public static List<Entity> entities = new ArrayList<Entity>();
    public static int selectedInstance = 0;
    public static List<Entity> entityMenu = new ArrayList<>();
    public static int selectedEntity;

    public static Player getPlayer() {
        Player player = null;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getName().equals("player")) {
                player = (Player) entities.get(i);
            }
        }
        return player;
    }

    public static Entity findEntity(String entityDialog) {
        for (int i = 0; i < entities.size(); i++) {
            Output.write(entities.get(i).dialog + " : " + entityDialog);
            try {
                if (entities.get(i).dialog.contains(entityDialog)) {
                    Output.write("found the entity");
                    return entities.get(i);
                }
            }
            catch (NullPointerException e) {}
        }
        Output.write("could not find the entity");
        return null;
    }
}