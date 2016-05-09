package workexpIT.merlin;

import workexpIT.merlin.attacks.Attack;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.EventReader;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;
import workexpIT.merlin.graphics.AttackAnimator;
import workexpIT.merlin.graphics.FaintAnimator;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.listeners.MouseListener;
import workexpIT.merlin.tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class GameLoop implements Runnable{


    //Game images
    public static BufferedImage save;
    public static BufferedImage menu;
    public static BufferedImage button;
    public static BufferedImage statusBar;

    public static Attack currentAttack = null;
    public static boolean playerTurn=true;
    public static boolean pause;

    public static Entity enemy;

    public static boolean fightMenu = false;
    public static boolean tileEditor = true;
    public static String dialogText;
    public static boolean startTigger;

    @Override
    public void run() {

        Output.write("loopdeloop");


        recordStart();

        if (Merlin.mode.equals(Merlin.Mode.GAME) && !pause) {

            if (startTigger) {
                Thread t = new Thread() {
                    public void run() {
                        Merlin.eventHandler.readEventData(EventReader.EventTriggerType.StartTrigger);
                    }
                };
                t.start();
                startTigger = false;
            }


            setLastLocs();
            movePlayer();

            runAI();
            Output.log("[GameLoop] Took " + recordEnd() + " milliseconds to calculate entity movements");
            recordStart();
            pause = true;
            JavaDrawer.runAnimation();
            Output.log("[GameLoop] Took " + recordEnd() + " milliseconds to tell the Java Drawer to run the animation");

        }
        else if (Merlin.mode.equals(Merlin.Mode.BATTLE) && !pause) {
            runTurn();
        }
        else if (Merlin.mode.equals(Merlin.Mode.EDITOR) && !pause && tileEditor) {
            if (MouseListener.pressed) {
                Point loc = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(loc, JavaDrawer.frame);
                if (DataReader.OS.contains("win")) {
                    loc.x = (int) (loc.getX() - 16);
                    loc.y = (int) (loc.getY() - 72);
                }
                else {
                    loc.y = (int) (loc.getY()-21);
                }
                System.out.println("PointerInfo Click " + loc.getX() + " " + loc.getY());
                int mapX = (int) (((loc.getX() - JavaDrawer.frame.getWidth() / 2) / JavaDrawer.scale - JavaDrawer.offsetX) / JavaDrawer.imageSize);
                int mapY = (int) ((loc.getY() - JavaDrawer.frame.getHeight() / 2) / JavaDrawer.scale - JavaDrawer.offsetY + JavaDrawer.imageSize) / JavaDrawer.imageSize;
                if (mapX >= 0 && mapY >= 0) {
                    if (loc.getX() < JavaDrawer.frame.getWidth()-JavaDrawer.editorMenuSize){
                        MouseListener.placeTile(WorldData.selectedTile, WorldData.selectedInstance, mapX, mapY);
                    }
                }
            }
        }
    }

    private void setLastLocs() {
        for (int i = 0; i < WorldData.entities.size(); i++) {
            WorldData.entities.get(i).lastLoc[0] = WorldData.entities.get(i).getX();
            WorldData.entities.get(i).lastLoc[1] = WorldData.entities.get(i).getY();
        }
    }

    private void runTurn() {
        if (WorldData.getPlayer().health <= 0 || enemy.health <=0) {
            pause = true;
            //TODO Battle over
            Output.write("Battle over");
            if (WorldData.getPlayer().health <=0) {
                Output.write("Player lost");
                new FaintAnimator(100,1,false,true);
                endBattle();
                //TODO Restart from last safe point
            }
            else {
                Output.write("Player won");
                //BattleAnimator.faint(true);
                WorldData.getPlayer().addXP(10*(((enemy.getLevel()-1)^2)+100)/4);
                Output.write("PLayer's xp = " + WorldData.getPlayer().xp);
                new FaintAnimator(100,1,false,false);
                WorldData.entities.remove(enemy);
                endBattle();
            }
        } else {
            if (playerTurn && currentAttack != null && !AttackAnimator.animate) {

                Output.write("Player attacks with " + currentAttack.getClass().getSimpleName());
                //TODO run player attack
                new AttackAnimator(60,2,false,currentAttack.getPlayerX(),currentAttack.getPlayerY(),currentAttack.getPlayerAniType(),currentAttack.texture);
            } else if (!playerTurn && !AttackAnimator.animate) {

                //TODO run enemy attack
                boolean attack = true;
                while (attack) {
                    currentAttack = enemy.attacks[(int) (Math.random() * 6)];
                    if (currentAttack != null) {

                        Output.write("Enemy attacks with " + currentAttack.getClass().getSimpleName());
                        new AttackAnimator(60,2,false,currentAttack.getEnemyX(),currentAttack.getEnemyY(),currentAttack.getEnemyAniType(),currentAttack.texture);
                        attack = false;

                    }
                }
            }
        }
    }

    private void endBattle() {
        int seconds = 3;
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Merlin.mode = Merlin.Mode.GAME;
                        pause = false;
                    }
                },
                (1000*seconds)
        );
    }

    public static void finishedAttack() {
        if (playerTurn) {
            if ((Math.random()*100) <= (0.005*Math.pow(enemy.speed-WorldData.getPlayer().speed,2.2)+10)){
                Output.write("The enemy dodged the attack!");
            }
            else {
                enemy.health = enemy.health - currentAttack.enemyDamage * WorldData.getPlayer().fortitude / 10;
                WorldData.getPlayer().health = WorldData.getPlayer().health - currentAttack.selfDamage * WorldData.getPlayer().fortitude / 10;
                enemy.mana = enemy.mana - currentAttack.manaDamage * WorldData.getPlayer().fortitude / 10;
                if ((currentAttack.enemyDamage * WorldData.getPlayer().fortitude / 10) > 0) Output.write("Caused " + (currentAttack.enemyDamage * WorldData.getPlayer().fortitude / 10) + " damage to the enemy's health");
                if ((currentAttack.manaDamage * WorldData.getPlayer().fortitude / 10) > 0)Output.write("Caused " + (currentAttack.manaDamage * WorldData.getPlayer().fortitude / 10) + " damage to the enemy's mana");
                if ((currentAttack.selfDamage * WorldData.getPlayer().fortitude / 10) > 0) Output.write("Caused " + (currentAttack.selfDamage * WorldData.getPlayer().fortitude / 10) + " damage to yourself");
                if ((currentAttack.enemyDamage * enemy.fortitude / 10) < 0) Output.write("Regened " + (currentAttack.enemyDamage * enemy.fortitude / 10) + " hp of the enemy's health");
                if ((currentAttack.manaDamage * enemy.fortitude / 10) < 0)Output.write("Regened " + (currentAttack.manaDamage * enemy.fortitude / 10) + " mana of the enemy's mana");
                if ((currentAttack.selfDamage * enemy.fortitude / 10) < 0) Output.write("Regened " + (currentAttack.selfDamage * enemy.fortitude / 10) + " hp of health to yourself");
            }
            WorldData.getPlayer().mana = WorldData.getPlayer().mana - currentAttack.manaCost * WorldData.getPlayer().fortitude / 10;
            if ((currentAttack.manaCost * WorldData.getPlayer().fortitude / 10) > 0)Output.write("Cost " + (currentAttack.manaCost * WorldData.getPlayer().fortitude / 10) + " mana to cast the attack");

            currentAttack = null;
            playerTurn = false;
            enemy.regenMana();
            Output.write("Enemy regened: " + enemy.manaRegen + " mana");

        }
        else {
            if ((Math.random()*100) <= (0.005*Math.pow(enemy.speed-WorldData.getPlayer().speed,2.2)+10)) {
                Output.write("DODGED");
            }
            else {
                WorldData.getPlayer().mana = WorldData.getPlayer().mana - currentAttack.manaDamage * enemy.fortitude / 10;
                WorldData.getPlayer().health = WorldData.getPlayer().health - currentAttack.enemyDamage * enemy.fortitude / 10;
                enemy.health = enemy.health - currentAttack.selfDamage * enemy.fortitude / 10;
                if ((currentAttack.enemyDamage * enemy.fortitude / 10) > 0) Output.write("Caused " + (currentAttack.enemyDamage * enemy.fortitude / 10) + " damage to the enemy's health");
                if ((currentAttack.manaDamage * enemy.fortitude / 10) > 0)Output.write("Caused " + (currentAttack.manaDamage * enemy.fortitude / 10) + " damage to the enemy's mana");
                if ((currentAttack.selfDamage * enemy.fortitude / 10) > 0) Output.write("Caused " + (currentAttack.selfDamage * enemy.fortitude / 10) + " damage to yourself");
                if ((currentAttack.enemyDamage * enemy.fortitude / 10) < 0) Output.write("Regened " + (currentAttack.enemyDamage * enemy.fortitude / 10) + " hp of the enemy's health");
                if ((currentAttack.manaDamage * enemy.fortitude / 10) < 0)Output.write("Regened " + (currentAttack.manaDamage * enemy.fortitude / 10) + " mana of the enemy's mana");
                if ((currentAttack.selfDamage * enemy.fortitude / 10) < 0) Output.write("Regened " + (currentAttack.selfDamage * enemy.fortitude / 10) + " hp of health to yourself");
            }
            enemy.mana = enemy.mana - currentAttack.manaCost * enemy.fortitude / 10;
            if ((currentAttack.manaCost * enemy.fortitude / 10) > 0)Output.write("Cost " + (currentAttack.manaCost * enemy.fortitude / 10) + " mana to cast the attack");
            currentAttack = null;
            playerTurn = true;
            WorldData.getPlayer().regenMana();
            Output.write("Player regened: " + WorldData.getPlayer().manaRegen + " mana");
        }
        if (enemy.health > enemy.healthMax) {
            enemy.health = enemy.healthMax;
        }
        if (WorldData.getPlayer().health > WorldData.getPlayer().healthMax) {
            WorldData.getPlayer().health = WorldData.getPlayer().healthMax;
        }
        if (enemy.mana > enemy.manaMax) {
            enemy.mana = enemy.manaMax;
        }
        if (WorldData.getPlayer().mana > WorldData.getPlayer().manaMax) {
            WorldData.getPlayer().mana = WorldData.getPlayer().manaMax;
        }
        Output.write("Player Health: " + WorldData.getPlayer().health + " Mana: " + WorldData.getPlayer().mana);
        Output.write("Enemy Health: " + enemy.health + " Mana: " + enemy.mana);
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
        pause = true;
        enemy = e;

        /*Thread zoom = new Thread("Zoom") {
            public void run() {
                for (int i = 0; i < 50; i++){
                    JavaDrawer.scale = JavaDrawer.scale + 0.25f;
                    //Output.write("ZOOOM");
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                Merlin.mode = Merlin.Mode.BATTLE;
                pause = false;
        /*    }
        };
        zoom.start();*/
    }

    public static void loadAllTextures() {
        save = ImageReader.loadImage("resources/graphics/save.png");
        statusBar = ImageReader.loadImage("resources/graphics/battle/entityStatusBackground.png");
        menu = ImageReader.loadImage("resources/graphics/battle/menuBackground.png");
        button = ImageReader.loadImage("resources/graphics/battle/buttonBackground.png");
        if (Merlin.mode == Merlin.Mode.EDITOR) {
            for (int i = 0; i < Reference.tileIds.length; i++) {
                //Output.write("Loading tile texture " + Reference.tileIds[i]);
                Tile tile = null;
                try {
                    tile = (Tile) Class.forName("workexpIT.merlin.tiles." + Reference.tileIds[i]).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Output.write("adding tile: " + tile.getClass().getSimpleName() + " to the menu");
                WorldData.menuTiles.add(tile);
                WorldData.animatedTiles.remove(tile);
            }
        }
        for (int i = 0; i < Reference.entities.length; i++) {
            Entity entity = null;
            try {
                Class<?> clazz = Class.forName("workexpIT.merlin.entities."+ Reference.entities[i]);
                Constructor<?> ctor = null;
                /*try {
                    ctor = clazz.getConstructor(int.class,int.class,int.class,int.class);
                    entity = (Entity) ctor.newInstance(0,0,0,1);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();*/
                    try {
                        ctor = clazz.getConstructor(int.class,int.class,int.class,int.class,String.class,boolean.class);
                        entity = (Entity) ctor.newInstance(0,0,0,1,null,false);
                    } catch (NoSuchMethodException e2) {
                        e2.printStackTrace();
                    }
                //}

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            WorldData.entityMenu.add(entity);
        }
    }

    public static void entityInteract(Entity entity1, Entity entity2) {
        //TODO move commented out code into each entities playerInteraction()
        if (entity1 == WorldData.getPlayer()) {
            /*if (entity2.getState() == Entity.STATE_AGGRESSIVE) {
                enemy = entity2;
                pause = true;
                JavaDrawer.startBattle();
            }
            else {*/
                entity2.playerInteraction();
            //}
        }
        else if (entity2 == WorldData.getPlayer()) {
            /*if (entity1.getState() == Entity.STATE_AGGRESSIVE) {
                enemy = entity1;
                pause = true;
                JavaDrawer.startBattle();
            }
            else {*/
                entity1.playerInteraction();
            //}
        }
        else {
            //Two NPC entities interact (do nothing at the moment)
        }
    }

    private static Timestamp recordStartTime;
    public static void recordStart() {
        recordStartTime = new Timestamp(new Date().getTime());
    }
    public static long recordEnd() {
        Timestamp recordEndTime = new Timestamp(new Date().getTime());
        long time = recordEndTime.getTime() - recordStartTime.getTime();
        return time;
    }

    public static void switchToEntityEditor() {
        if (tileEditor) {
            tileEditor = false;
        }
        else {
            tileEditor = true;
        }
    }

    public static void displayDialog(String text) {
        pause = true;
        dialogText = text;
        JavaDrawer.drawDialog(text);
    }

    public static void triggerLocationEvent(int[] location) {
        Merlin.eventHandler.readEventData(EventReader.EventTriggerType.LocationTrigger,location);
    }
}
