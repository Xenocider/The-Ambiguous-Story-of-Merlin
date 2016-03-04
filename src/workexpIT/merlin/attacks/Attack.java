package workexpIT.merlin.attacks;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.data.ImageReader;

import java.awt.image.BufferedImage;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Attack {
    public static int startX;
    public static int startY;
    public static int textureX;
    public static int textureY;
    public static boolean enemyThrust;
    public static boolean playerThrust;
    public static boolean enemyFlinch;
    public static boolean playerFlinch;
    public static boolean attackAnimationRun = false;

    public static void start(BufferedImage texture, int x, int y, AnimationType aniType) {
        textureX = x;
        textureY = y;
        startX = x;
        startY = y;
        animationType = aniType;
        attackAnimationRun = true;
        animationTexture = texture;
        if (GameLoop.playerTurn) {
            playerThrust = true;
            enemyThrust = false;
        }
        else {
            enemyThrust = true;
            playerThrust = false;
        }
    }
    public static BufferedImage animationTexture;

    public void runEnemyAnimation() {
        //For child classes
        GameLoop.finishedAttack();
    }

    public static void react() {
        if (GameLoop.currentAttack.enemyDamage > 0 || GameLoop.currentAttack.manaDamage > 0) {
            if (GameLoop.playerTurn) {
                enemyFlinch = true;
            }
            else {
                playerFlinch = true;
            }
        } else if (GameLoop.currentAttack.selfDamage > 0) {
            if (GameLoop.playerTurn) {
                playerFlinch = true;
            }
            else {
                enemyFlinch = true;
            }
        }
        else {
            GameLoop.finishedAttack();
        }
    }

    public enum AnimationType {STILL,UP,DOWN,TOWARDS_ENEMY,TOWARDS_PLAYER}
    public static AnimationType animationType;
    public static int animationStage = 0;
    public static final int maxAnimationStage = 40;

    public void runPlayerAnimation() {
        //For child classes
        GameLoop.finishedAttack();
    }

    public enum DamageType {NORMAL,FIRE,WATER,EARTH,AIR};

    public int manaDamage;
    public int manaCost;
    public int enemyDamage;
    public int selfDamage;
    public DamageType damageType;
    public String description = "default description";
    public BufferedImage texture;

    public Attack(int dmana, int mana, DamageType dt,int enemyD, int selfD, String desc) {
        manaDamage = dmana;
        manaCost = mana;
        damageType = dt;
        enemyDamage = enemyD;
        selfDamage = selfD;
        description = desc;
        texture = ImageReader.loadImage("resources/graphics/attacks/"+this.getClass().getSimpleName()+".png");
    }


}
