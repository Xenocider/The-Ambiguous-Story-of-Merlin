package workexpIT.merlin;

import workexpIT.merlin.attacks.Attack;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.BattleAnimator;
import workexpIT.merlin.graphics.JavaDrawer;

import java.awt.image.BufferedImage;
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

    @Override
    public void run() {

        if (Merlin.mode.equals(Merlin.Mode.GAME) && !pause) {
            movePlayer();

            runAI();

            checkForBattle();
        }
        else if (Merlin.mode.equals(Merlin.Mode.BATTLE) && !pause) {
            runTurn();
        }
    }

    private void runTurn() {
        if (WorldData.getPlayer().health <= 0 || enemy.health <=0) {
            pause = true;
            //TODO Battle over
            Output.write("Battle over");
            if (WorldData.getPlayer().health <=0) {
                Output.write("Player lost");
                JavaDrawer.pfaint = true;
                endBattle();
                //TODO Restart from last safe point
            }
            else {
                Output.write("Player won");
                //BattleAnimator.faint(true);
                WorldData.getPlayer().addXP(10*(((enemy.getLevel()-1)^2)+100)/4);
                Output.write("PLayer's xp = " + WorldData.getPlayer().xp);
                JavaDrawer.efaint = true;
                WorldData.entities.remove(enemy);
                endBattle();
            }
        } else {
            if (playerTurn && currentAttack != null && !Attack.attackAnimationRun && !Attack.playerFlinch && !Attack.enemyFlinch) {

                Output.write("Player attacks with " + currentAttack.getClass().getSimpleName());
                //TODO run player attack
                currentAttack.runPlayerAnimation();
            } else if (!playerTurn && !Attack.attackAnimationRun && !Attack.playerFlinch && !Attack.enemyFlinch) {

                //TODO run enemy attack
                boolean attack = true;
                while (attack) {
                    currentAttack = enemy.attacks[(int) (Math.random() * 6)];
                    if (currentAttack != null) {

                        Output.write("Enemy attacks with " + currentAttack.getClass().getSimpleName());
                        currentAttack.runEnemyAnimation();
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
                        JavaDrawer.pfaint = false;
                        JavaDrawer.efaint = false;
                        BattleAnimator.enemyOffsetY = 0;
                        BattleAnimator.enemyOffsetY = 0;
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

    private void checkForBattle() {
            for (int i = 0; i < WorldData.entities.size(); i++) {
                if (WorldData.entities.get(i).getX() == WorldData.getPlayer().getX() && WorldData.entities.get(i).getY() == WorldData.getPlayer().getY() && !WorldData.entities.get(i).equals(WorldData.getPlayer())) {
                    enemy = WorldData.entities.get(i);
                    pause = true;
                    JavaDrawer.startBattle();
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
                }
                Merlin.mode = Merlin.Mode.BATTLE;
                pause = false;
            }
        };
        zoom.start();*/
    }

    public static void loadAllTextures() {
        save = ImageReader.loadImage("resources/graphics/save.png");
        statusBar = ImageReader.loadImage("resources/graphics/battle/entityStatusBackground.png");
        menu = ImageReader.loadImage("resources/graphics/battle/menuBackground.png");
        button = ImageReader.loadImage("resources/graphics/battle/buttonBackground.png");
    }
}
