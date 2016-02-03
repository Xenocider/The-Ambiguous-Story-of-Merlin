package workexpIT.merlin;

import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.graphics.Drawer;


public class Merlin {

    public static void main(String[] args) {
        DataReader.loadMap("TEST");
        System.out.println(WorldData.tiles[0][1].getId());
        Drawer.start();
    }
}
