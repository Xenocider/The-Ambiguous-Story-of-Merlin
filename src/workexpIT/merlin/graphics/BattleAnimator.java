package workexpIT.merlin.graphics;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;

public class BattleAnimator {

    public static int playerOffsetX;
    public static int playerOffsetY = 0;
    public static int enemyOffsetX;
    public static int enemyOffsetY = 0;

    public static void faint(boolean enemy) {

        if (enemy) {
            Thread faint = new Thread("Faint") {
                public void run() {
                    for (int i = 0; i < 500; i++){
                        BattleAnimator.enemyOffsetY = BattleAnimator.enemyOffsetY +4;
                        //Output.write("fainting...");
                        JavaDrawer.frame.repaint();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            faint.start();
        }
        else {
            Thread faint = new Thread("Faint") {
                public void run() {
                    for (int i = 0; i < 500; i++){
                        BattleAnimator.playerOffsetY = BattleAnimator.playerOffsetY +4;
                        //Output.write("fainting...");
                        JavaDrawer.frame.repaint();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            faint.start();
        }
    }


}
