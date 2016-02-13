package workexpIT.merlin;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;

public class GameLoop {
    public static void run() {

        movePlayer();

        runAI();



    }

    private static void movePlayer() {
        if (Merlin.keyListener.upPressed || Merlin.keyListener.moveUpTemp) {
            WorldData.getPlayer().move(Entity.MOVE_UP);
            Merlin.keyListener.moveUpTemp = false;
        }
        if (Merlin.keyListener.leftPressed || Merlin.keyListener.moveLeftTemp) {
            WorldData.getPlayer().move(Entity.MOVE_LEFT);
            Merlin.keyListener.moveLeftTemp = false;
        }
        if (Merlin.keyListener.downPressed || Merlin.keyListener.moveDownTemp) {
            WorldData.getPlayer().move(Entity.MOVE_DOWN);
            Merlin.keyListener.moveDownTemp = false;
        }
        if (Merlin.keyListener.rightPressed || Merlin.keyListener.moveRightTemp) {
            WorldData.getPlayer().move(Entity.MOVE_RIGHT);
            Merlin.keyListener.moveRightTemp = false;
        }
    }

    private static void runAI() {
        for (int i = 0; i < WorldData.entities.size(); i++) {
            WorldData.entities.get(i).runAI();
        }
    }
}
