package workexpIT.merlin.graphics;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Output;

/**
 * Created by ict11 on 2016-03-07.
 */
public class FaintAnimator extends Animator {

    boolean player;

    public FaintAnimator(int stages, int speedFactor, boolean shouldLoop, boolean player) {
        super(stages, speedFactor, shouldLoop);
        this.player = player;
    }

    @Override
    public void runAnimation() {
        if (player) {
            JavaDrawer.playerAnimationOffsetY = JavaDrawer.playerAnimationOffsetY + 8;

        }
        else {
            JavaDrawer.enemyAnimationOffsetY = JavaDrawer.enemyAnimationOffsetY + 8;
            Output.write(""+JavaDrawer.enemyAnimationOffsetY);
        }
    }

    @Override
    public void endAnimation() {
        if (player) {
            JavaDrawer.playerAnimationOffsetY = JavaDrawer.playerAnimationOffsetY - 8*maxStages;
        }
        else {
            JavaDrawer.enemyAnimationOffsetY = JavaDrawer.enemyAnimationOffsetY - 8*maxStages;
        }
    }

}
