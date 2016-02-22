package workexpIT.merlin;

import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;
import workexpIT.merlin.graphics.Animator;
import workexpIT.merlin.graphics.Drawer;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.listeners.KeyListener;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Merlin implements Runnable{

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public static String platform;

    public static KeyListener keyListener;

    public static ScheduledFuture drawer;


    public static void main(String[] args) {

        //JFrame frame = new JFrame("The Ambiguous Story of Merlin");
        //frame.setSize(300,400);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setVisible(true);
        JavaDrawer.init();

//        keyListener = new KeyListener();
        WorldData.entities.add(new Player(1,1,1));
//        try{platform = args[0];}
//        catch (Exception e) {e.printStackTrace();}

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        drawer = executor.scheduleWithFixedDelay(new JavaDrawer(),0,100,TimeUnit.MILLISECONDS);
        //drawer = executor.scheduleWithFixedDelay(new Drawer(), 0, 100, TimeUnit.MILLISECONDS);
        //drawer.start();

        DataReader.loadMap("field");

        //drawer.run();


        //Drawer d = new Drawer();
        //d.init2();

    }

    @Override
    public void run() {

    }


}
