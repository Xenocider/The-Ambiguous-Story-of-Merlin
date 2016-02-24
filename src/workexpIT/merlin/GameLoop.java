package workexpIT.merlin;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.JavaDrawer;

public class GameLoop implements Runnable{

    @Override
    public void run() {

        if (Merlin.mode.equals(Merlin.Mode.GAME)) {
            movePlayer();

            runAI();
        }
        else if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            moveScreen();
        }
        Output.write(WorldData.getPlayer().getX() + " " + WorldData.getPlayer().getY());

    }

    private void moveScreen() {
        if (Merlin.keyListener.upPressed || Merlin.keyListener.upTemp) {
            JavaDrawer.offsetY = JavaDrawer.offsetY + 1 * JavaDrawer.imageSize;
            Merlin.keyListener.upTemp = false;
        }
        if (Merlin.keyListener.leftPressed || Merlin.keyListener.leftTemp) {
            JavaDrawer.offsetX = JavaDrawer.offsetX + 1 * JavaDrawer.imageSize;
            Merlin.keyListener.leftTemp = false;
        }
        if (Merlin.keyListener.downPressed || Merlin.keyListener.downTemp) {
            JavaDrawer.offsetY = JavaDrawer.offsetY - 1 * JavaDrawer.imageSize;
            Merlin.keyListener.downTemp = false;
        }
        if (Merlin.keyListener.rightPressed || Merlin.keyListener.rightTemp) {
            JavaDrawer.offsetX = JavaDrawer.offsetX - 1 * JavaDrawer.imageSize;
            Merlin.keyListener.rightTemp = false;
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
}
