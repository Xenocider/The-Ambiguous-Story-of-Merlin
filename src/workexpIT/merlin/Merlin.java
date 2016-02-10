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


    public static void main(String[] args) {
        DataReader.loadMap("field");
        Thread drawer = new Thread(new Drawer(), "Drawer");
        //drawer.start();
        WorldData.entities.add(new Player(1,1));

        drawer.run();


        //Drawer d = new Drawer();
        //d.init2();
    }

    @Override
    public void run() {

    }

    public static void movePlayer(int direction) {
        //0 = up, 1 = right, 2 = down, 3 = left
        for (int i = 0; i < WorldData.entities.size(); i++) {
            if (WorldData.entities.get(i).getName() == "player") {
                Player player = (Player) WorldData.entities.get(i);
                switch (direction) {
                    case UP:
                        try {
                            if (WorldData.tiles[player.getX()][player.getY() - 1].movingOnToTile()) {
                                player.setY(player.getY() - 1);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            Output.write("Player at edge of map!");
                        }
                        catch (NullPointerException e) {
                            Output.write("Player at edge of map!");
                        }
                        break;
                    case RIGHT:
                        try {
                        if (WorldData.tiles[player.getX()+1][player.getY()].movingOnToTile()) {
                            player.setX(player.getX() + 1);
                        }
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            Output.write("Player at edge of map!");
                        }
                        catch (NullPointerException e) {
                            Output.write("Player at edge of map!");
                        }
                        break;
                    case DOWN:
                        try {
                        if (WorldData.tiles[player.getX()][player.getY()+1].movingOnToTile()) {
                            player.setY(player.getY() + 1);
                        }
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            Output.write("Player at edge of map!");
                        }
                        catch (NullPointerException e) {
                            Output.write("Player at edge of map!");
                        }
                        break;
                    case LEFT:
                        try {
                        if (WorldData.tiles[player.getX()-1][player.getY()].movingOnToTile()) {
                            player.setX(player.getX() - 1);
                        }
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            Output.write("Player at edge of map!");
                        }
                        catch (NullPointerException e) {
                            Output.write("Player at edge of map!");
                        }
                        break;
                }
            }
        }
    }
}
