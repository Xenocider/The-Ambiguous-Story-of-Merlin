package workexpIT.merlin.graphics;

import workexpIT.merlin.Output;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Animator {

    public static List<Animator> currentAnimators = new ArrayList<Animator>();

    public int count = 0;
    public int stage = 0;
    public int maxStages;
    public int stageStep;
    public boolean shouldLoop = false;

    public Animator(int stages,int speedFactor, boolean shouldLoop) {
        maxStages = stages;
        stageStep = speedFactor;
        currentAnimators.add(this);
        this.shouldLoop = shouldLoop;
        runAdditionalStartCode();
    }

    public void run() {
        Output.write("if (" + count + "==" + stage + "*" + stageStep + "+" + stageStep + ") (It equals " + (stage*stageStep+stageStep)+")");
        if (count == stage*stageStep+stageStep) {
            stage = stage + 1;
            if (stage > maxStages) {
                if (shouldLoop) {
                    stage = 0;
                    count = 0;
                } else {
                    endAnimation();
                    close();
                }
            }
            else {
                runAnimation();
            }
        }
        count = count + 1;
    }

    public void close() {
        currentAnimators.remove(this);
    }

    public void endAnimation() {
        //For child classes
    }


    public void runAnimation() {
        //For child classes
    }


}
