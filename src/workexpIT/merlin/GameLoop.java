package workexpIT.merlin;

import workexpIT.merlin.data.WorldData;

public class GameLoop implements Runnable {
    @Override
    public void run() {

        runAI();



    }

    private void runAI() {
        for (int i = 0; i < WorldData.entities.size(); i++) {
            WorldData.entities.get(i).runAI();
        }
    }
}
