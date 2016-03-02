package workexpIT.merlin;

import workexpIT.merlin.attacks.Attack;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.BattleAnimator;
import workexpIT.merlin.graphics.JavaDrawer;

public class GameLoop implements Runnable{
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
        if (playerTurn && currentAttack != null) {
            enemy.regenMana();
            Output.write("Player attacks with " + currentAttack.getClass().getSimpleName());
            //TODO run player attack
            enemy.health = enemy.health - currentAttack.enemyDamage;
            WorldData.getPlayer().health = WorldData.getPlayer().health - currentAttack.selfDamage;
            WorldData.getPlayer().mana = WorldData.getPlayer().mana - currentAttack.manaCost;
            currentAttack = null;
            playerTurn = false;
        }
        else if (!playerTurn) {
            WorldData.getPlayer().regenMana();
            //TODO run enemy attack
            boolean attack = true;
            while (attack) {
                currentAttack = enemy.attacks[(int)(Math.random()*6)];
                if (currentAttack != null) {
                    WorldData.getPlayer().health = WorldData.getPlayer().health - currentAttack.enemyDamage;
                    enemy.health = enemy.health - currentAttack.selfDamage;
                    enemy.mana = enemy.mana - currentAttack.manaCost;
                    Output.write("Enemy attacks with " + currentAttack.getClass().getSimpleName());
                    currentAttack = null;
                    playerTurn = true;
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
                BattleAnimator.faint(false);
            }
            else {
                Output.write("Player won");
                JavaDrawer.enemyFaint = true;
            }
        }
    }

    private void checkForBattle() {
            for (int i = 0; i < WorldData.entities.size(); i++) {
                if (WorldData.entities.get(i).getX() == WorldData.getPlayer().getX() && WorldData.entities.get(i).getY() == WorldData.getPlayer().getY() && !WorldData.entities.get(i).equals(WorldData.getPlayer())) {
                        GameLoop.startBattle(WorldData.getPlayer(),WorldData.entities.get(i));
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
        enemy = e;
        pause = true;
        Thread zoom = new Thread("Zoom") {
            public void run() {
                for (int i = 0; i < 50; i++){
                    JavaDrawer.scale = JavaDrawer.scale + 0.25f;
                    Output.write("ZOOOM");
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
        zoom.start();
    }
}
