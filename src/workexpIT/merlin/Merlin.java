package workexpIT.merlin;

import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;
import workexpIT.merlin.graphics.Drawer;


public class Merlin implements Runnable{

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public static String platform;


    public static void main(String[] args) {
        try{platform = args[0];}
        catch (Exception e) {e.printStackTrace();}
        DataReader.loadMap("field");
        Thread drawer = new Thread(new Drawer(), "Drawer");
        //drawer.start();
        WorldData.entities.add(new Player(1,1,1));

        drawer.run();


        //Drawer d = new Drawer();
        //d.init2();
    }

    @Override
    public void run() {

    }


}
