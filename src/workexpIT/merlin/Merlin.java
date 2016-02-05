package workexpIT.merlin;

import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;


public class Merlin {

    public static void main(String[] args) {
        DataReader.loadMap("TEST");
        Thread drawer = new Thread(new Drawer(), "Drawer");
        drawer.start();
    }
}
