package workexpIT.merlin;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.BattleAnimator;
import workexpIT.merlin.graphics.JavaDrawer;

public class GameLoop implements Runnable{
    public static boolean pause;

    public static Entity enemy;

    public static boolean fightMenu = false;

    @Override
    public void run() {

        if (Merlin.mode.equals(Merlin.Mode.GAME) && !pause) {
            movePlayer();

            runAI();

            checkForBattle();
        }
        else if (Merlin.mode.equals(Merlin.Mode.BATTLE)) {
            //TODO Battle code
        }
    }

    private void checkForBattle() {
            for (int i = 0; i < WorldData.entities.size(); i++) {
                if (WorldData.entities.get(i).getX() == WorldData.getPlayer().getX() && WorldData.entities.get(i).getY() == WorldData.getPlayer().getY() && !WorldData.entities.get(i).equals(WorldData.getPlayer())) {
                        GameLoop.startBattle(WorldData.getPlayer(),WorldData.entities.get(i));
                }
            }
    }


    private static void movePlayer() {
        if (Merlin.keyListener.upPressed || Merlin.keyListener.upTemp) {
            WorldData.getPlayer().move(Entity.MOVE_UP);
            Merlin.keyListener.upTemp = false;
        }
        if (Merlin.keyListener.leftPressed || Merlin.keyListener.leftTemp) {
            WorldData.getPlayer().move(Entity.MOVE_LEFT);
            Merlin.keyListener.leftTemp = false;
        }
        if (Merlin.keyListener.downPressed || Merlin.keyListener.downTemp) {
            WorldData.getPlayer().move(Entity.MOVE_DOWN);
            Merlin.keyListener.downTemp = false;
        }
        if (Merlin.keyListener.rightPressed || Merlin.keyListener.rightTemp) {
            WorldData.getPlayer().move(Entity.MOVE_RIGHT);
            Merlin.keyListener.rightTemp = false;
        }
    }

    private static void runAI() {
        for (int i = 0; i < WorldData.entities.size(); i++) {
            WorldData.entities.get(i).runAI();
        }
    }

    public static void startBattle(Entity player, Entity e) {
        enemy = e;
        pause = true;
        Thread zoom = new Thread("Zoom") {
            public void run() {
                for (int i = 0; i < 50; i++){
                    JavaDrawer.scale = JavaDrawer.scale + 0.25f;
                    Output.write("ZOOOM");
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Merlin.mode = Merlin.Mode.BATTLE;
            }
        };
        zoom.start();
    }
}
