package workexpIT.merlin;

import workexpIT.merlin.attacks.Attack;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.BattleAnimator;
import workexpIT.merlin.graphics.JavaDrawer;

import java.awt.image.BufferedImage;

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
        if (playerTurn && currentAttack != null && !Attack.attackAnimationRun && !Attack.playerFlinch && !Attack.enemyFlinch) {
            WorldData.getPlayer().regenMana();

            Output.write("Player attacks with " + currentAttack.getClass().getSimpleName());
            //TODO run player attack
            currentAttack.runPlayerAnimation();
        }
        else if (!playerTurn && !Attack.attackAnimationRun && !Attack.playerFlinch && !Attack.enemyFlinch) {
            enemy.regenMana();

            //TODO run enemy attack
            boolean attack = true;
            while (attack) {
                currentAttack = enemy.attacks[(int)(Math.random()*6)];
                if (currentAttack != null) {

                    Output.write("Enemy attacks with " + currentAttack.getClass().getSimpleName());
                    currentAttack.runEnemyAnimation();
                    attack = false;

                }
            }
        }
        if (WorldData.getPlayer().health <= 0 || enemy.health <=0) {
            pause = true;
            //TODO Battle over
            Output.write("Battle over");
            if (WorldData.getPlayer().health <=0) {
                Output.write("Player lost");
                JavaDrawer.pfaint = true;
            }
            else {
                Output.write("Player won");
                //BattleAnimator.faint(true);
                JavaDrawer.efaint = true;
                WorldData.entities.remove(enemy);
                Merlin.mode = Merlin.Mode.GAME;
                pause = false;
            }
        }
    }

    public static void finishedAttack() {
        if (playerTurn) {
            WorldData.getPlayer().mana = WorldData.getPlayer().mana - currentAttack.manaCost;
            enemy.health = enemy.health - currentAttack.enemyDamage;
            WorldData.getPlayer().health = WorldData.getPlayer().health - currentAttack.selfDamage;
            enemy.mana = enemy.mana - currentAttack.manaDamage;
            currentAttack = null;
            playerTurn = false;
        }
        else {
            enemy.mana = enemy.mana - currentAttack.manaCost;
            WorldData.getPlayer().mana = WorldData.getPlayer().mana - currentAttack.manaDamage;
            WorldData.getPlayer().health = WorldData.getPlayer().health - currentAttack.enemyDamage;
            enemy.health = enemy.health - currentAttack.selfDamage;
            currentAttack = null;
            playerTurn = true;
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
