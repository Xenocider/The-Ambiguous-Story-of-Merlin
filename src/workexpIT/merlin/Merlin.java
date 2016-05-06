package workexpIT.merlin;

import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.SoundHandler;
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

    public static void startGame() {
        mode = Mode.GAME;
        GameLoop.loadAllTextures();
        JavaDrawer.frame.getGraphics().drawImage(ImageReader.loadImage("resources/graphics/loadscreen.png"),0,0,null);
        WorldData.entities.add(new Player(0, 0, 1));
        Output.log("Took " + Output.recordEnd() + " milliseconds to add the Player");
        Output.recordStart();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        gameLoop = executor.scheduleWithFixedDelay(new GameLoop(), 0, 250, TimeUnit.MILLISECONDS);
        Output.log("Took " + Output.recordEnd() + " milliseconds to schedule a thread for the GameLoop");
        Output.recordStart();
        DataReader.loadMap("test");
        Output.log("Took " + Output.recordEnd() + " milliseconds to load the map");
    }

    public static void startEditor() {
        mode = Mode.EDITOR;
        GameLoop.loadAllTextures();
        String map = JavaDrawer.textField.getText();
        JavaDrawer.frame.getGraphics().drawImage(ImageReader.loadImage("resources/graphics/loadscreen.png"),0,0,null);
        DataReader.editMap(map);
        Output.log("Took " + Output.recordEnd() + " milliseconds to load the map");
        Output.recordStart();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        gameLoop = executor.scheduleWithFixedDelay(new GameLoop(), 0, 250, TimeUnit.MILLISECONDS);
        Output.log("Took " + Output.recordEnd() + " milliseconds to schedule a thread for the GameLoop");
    }

    public enum Mode {GAME, EDITOR, BATTLE, MENU}

    public static JavaDrawer jDrawer =new JavaDrawer();

    public static void main(String[] args) {

        SoundHandler.playBackgroundMusic();

        if (args.length == 2 && args[0].equals("mapeditor")) {
            mode = Mode.EDITOR;
        } else {
            mode = Mode.GAME;
        }

        mode = Mode.MENU;
        Output.recordStart();
        Output.log("Took " + Output.recordEnd() + " milliseconds to load the game's textures");
Output.recordStart();


        Output.log("Took " + Output.recordEnd() + " milliseconds to set the gamemode");
        Output.recordStart();
        JavaDrawer.init();
        Output.log("Took " + Output.recordEnd() + " milliseconds to initialize the JavaDrawer");
        Output.recordStart();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        drawer = executor.scheduleWithFixedDelay(jDrawer, 0, (int)(1f/fps*1000f), TimeUnit.MILLISECONDS);
        Output.log("Took " + Output.recordEnd() + " milliseconds to schedule a thread for the JavaDrawer");
        Output.recordStart();
        //drawer = executor.scheduleWithFixedDelay(new JavaDrawer(), 0, 3, TimeUnit.MILLISECONDS);
        if (mode == Mode.EDITOR) {
            DataReader.editMap(args[1]);
            Output.log("Took " + Output.recordEnd() + " milliseconds to load the map");
            Output.recordStart();
            gameLoop = executor.scheduleWithFixedDelay(new GameLoop(), 0, 250, TimeUnit.MILLISECONDS);
            Output.log("Took " + Output.recordEnd() + " milliseconds to schedule a thread for the GameLoop");
        }
        else if (mode == Mode.GAME){
            //WorldData.entities.add(new Player(14, 17, 10));
            WorldData.entities.add(new Player(0, 0, 1));
            Output.log("Took " + Output.recordEnd() + " milliseconds to add the Player");
            Output.recordStart();
            gameLoop = executor.scheduleWithFixedDelay(new GameLoop(), 0, 250, TimeUnit.MILLISECONDS);
            Output.log("Took " + Output.recordEnd() + " milliseconds to schedule a thread for the GameLoop");
            Output.recordStart();
            DataReader.loadMap("test");
            Output.log("Took " + Output.recordEnd() + " milliseconds to load the map");

        }


    }

    @Override
    public void run() {

    }


}
