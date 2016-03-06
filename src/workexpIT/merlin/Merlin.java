package workexpIT.merlin;

import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Bob;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;
import workexpIT.merlin.graphics.Animator;
import workexpIT.merlin.graphics.Drawer;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.listeners.JavaKeyListener;
import workexpIT.merlin.listeners.KeyListener;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Merlin implements Runnable{

    public static final float fps = 60;

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public static String platform;

    //public static KeyListener keyListener;
    public static JavaKeyListener keyListener = new JavaKeyListener();

    public static ScheduledFuture drawer;
    public static ScheduledFuture gameLoop;

    public static Mode mode;

    public enum Mode {GAME, EDITOR, BATTLE}

    public static JavaDrawer jDrawer =new JavaDrawer();

    public static void main(String[] args) {
        GameLoop.loadAllTextures();

            if (args.length == 2 && args[0].equals("mapeditor")) {
                mode = Mode.EDITOR;
            } else {
                mode = Mode.GAME;
            }


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        JavaDrawer.init();
        drawer = executor.scheduleWithFixedDelay(jDrawer, 0, (int)(1f/fps*1000f), TimeUnit.MILLISECONDS);
        //drawer = executor.scheduleWithFixedDelay(new JavaDrawer(), 0, 3, TimeUnit.MILLISECONDS);
        if (mode == Mode.EDITOR) {
            DataReader.editMap(args[1]);
            gameLoop = executor.scheduleWithFixedDelay(new GameLoop(), 0, 250, TimeUnit.MILLISECONDS);
        }
        else {
            WorldData.entities.add(new Player(0, 0, 10));
            gameLoop = executor.scheduleWithFixedDelay(new GameLoop(), 0, 250, TimeUnit.MILLISECONDS);

            DataReader.loadMap("null");

        }

    }

    @Override
    public void run() {

    }


}
