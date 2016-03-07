package workexpIT.merlin.graphics;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Output;
import workexpIT.merlin.data.WorldData;

import java.awt.image.BufferedImage;

/**
 * Created by ict11 on 2016-03-07.
 */
public class AttackAnimator extends Animator {
    public static int startX;
    public static int startY;
    public static int textureX;
    public static int textureY;
    public static boolean enemyThrust;
    public static boolean playerThrust;
    public static boolean enemyFlinch;
    public static boolean playerFlinch;
    public static boolean attackAnimationRun = false;
    public static float flinchCount = 0;
    public static float blinkSpeed = 0.2f;
    public static int flichSpeed = 4;
    public static float flinchPercent = 0.3f;
    public static boolean animate = false;


    public static BufferedImage animationTexture;

    public enum AnimationType {STILL,UP,DOWN,TOWARDS_ENEMY, animationType, TOWARDS_PLAYER}
    public static AnimationType animationType;

    public AttackAnimator(int stages, int speedFactor, boolean shouldLoop,int x, int y, AnimationType aniType, BufferedImage texture) {
        super(stages, speedFactor, shouldLoop);
        animate = true;
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

    @Override
    public void runAnimation() {
        if (stage < (maxStages-maxStages*flinchPercent)) {
            if (AttackAnimator.playerThrust) {
                if (stage < (maxStages-maxStages*flinchPercent)/2) {
                    JavaDrawer.playerAnimationOffsetX = JavaDrawer.playerAnimationOffsetX + flichSpeed;
            }
            else if (stage > (maxStages-maxStages*flinchPercent)/2){
                JavaDrawer.playerAnimationOffsetX = JavaDrawer.playerAnimationOffsetX - flichSpeed;
            }
        }
        if (AttackAnimator.enemyThrust) {
            if (stage < (maxStages-maxStages*flinchPercent)/2) {
                JavaDrawer.enemyAnimationOffsetX = JavaDrawer.enemyAnimationOffsetX - flichSpeed;

            }
            else if (stage > (maxStages-maxStages*flinchPercent)/2){
                JavaDrawer.enemyAnimationOffsetX = JavaDrawer.enemyAnimationOffsetX + flichSpeed;
            }
        }
            switch (AttackAnimator.animationType) {
                case TOWARDS_ENEMY:
                    Output.write(((JavaDrawer.enemyX + GameLoop.enemy.battleSprite.getWidth() / 2 - GameLoop.currentAttack.texture.getWidth() / 2 - AttackAnimator.startX) / (int)(maxStages-maxStages*flinchPercent) * stage + AttackAnimator.startX)+"");
                    JavaDrawer.attackAnimationOffsetX = (JavaDrawer.enemyX + GameLoop.enemy.battleSprite.getWidth() / 2 - GameLoop.currentAttack.texture.getWidth() / 2 - AttackAnimator.startX) / (int)(maxStages-maxStages*flinchPercent) * stage + AttackAnimator.startX;
                    JavaDrawer.attackAnimationOffsetY = (JavaDrawer.enemyY + GameLoop.enemy.battleSprite.getHeight() / 2 - GameLoop.currentAttack.texture.getHeight() / 2 - AttackAnimator.startY) / (int)(maxStages-maxStages*flinchPercent) * stage + AttackAnimator.startY;
                case STILL:
                    //TODO ****Most if not all animations should be stills with animated sprites that simulate motion****
                    break;
                case UP:
                    JavaDrawer.attackAnimationOffsetY = JavaDrawer.attackAnimationOffsetY - 15;
                    break;
                case DOWN:
                    break;
                case TOWARDS_PLAYER:
                    JavaDrawer.attackAnimationOffsetX = (JavaDrawer.playerX + WorldData.getPlayer().battleSprite.getWidth() / 2 - GameLoop.currentAttack.texture.getWidth() / 2 - AttackAnimator.startX) / (int)(maxStages-maxStages*flinchPercent) * stage + AttackAnimator.startX;
                    JavaDrawer.attackAnimationOffsetY = (JavaDrawer.playerY + WorldData.getPlayer().battleSprite.getHeight() / 4 - GameLoop.currentAttack.texture.getHeight() / 4 - AttackAnimator.startY) / (int)(maxStages-maxStages*flinchPercent) * stage + AttackAnimator.startY;
                    break;
            }
        }
        if (stage > (maxStages-maxStages*flinchPercent)) {
            animationTexture = null;
            if (((int) (stage / blinkSpeed) & 1) == 0) {
                if (GameLoop.currentAttack.enemyDamage > 0 || GameLoop.currentAttack.manaDamage > 0) {
                    if (GameLoop.playerTurn) {
                        if (JavaDrawer.eFlinch) JavaDrawer.eFlinch = false;
                        else JavaDrawer.eFlinch = true;
                    } else {
                        if (JavaDrawer.pFlinch) JavaDrawer.pFlinch = false;
                        else JavaDrawer.pFlinch = true;
                    }
                } else if (GameLoop.currentAttack.selfDamage > 0) {
                    if (GameLoop.playerTurn) {
                        if (JavaDrawer.pFlinch) JavaDrawer.pFlinch = false;
                        else JavaDrawer.pFlinch = true;
                    } else {
                        if (JavaDrawer.eFlinch) JavaDrawer.eFlinch = false;
                        else JavaDrawer.eFlinch = true;
                    }
                } else {
                    flinchCount = 0;
                    JavaDrawer.pFlinch = false;
                    JavaDrawer.eFlinch = false;
                    GameLoop.finishedAttack();
                }
            }
            if (stage > (maxStages - maxStages * flinchPercent)) {
                if ((!GameLoop.playerTurn && (GameLoop.currentAttack.enemyDamage > 0 || GameLoop.currentAttack.manaDamage > 0)) || GameLoop.playerTurn && (GameLoop.currentAttack.selfDamage > 0)) {
                    if (stage < ((maxStages - maxStages * flinchPercent)+(maxStages * flinchPercent)/2)) {
                        JavaDrawer.playerAnimationOffsetX = JavaDrawer.playerAnimationOffsetX - flichSpeed;
                    } else if (stage > ((maxStages - maxStages * flinchPercent)+(maxStages * flinchPercent)/2)) {
                        JavaDrawer.playerAnimationOffsetX = JavaDrawer.playerAnimationOffsetX + flichSpeed;
                    }
                }
                if ((GameLoop.playerTurn && (GameLoop.currentAttack.enemyDamage > 0 || GameLoop.currentAttack.manaDamage > 0)) || !GameLoop.playerTurn && (GameLoop.currentAttack.selfDamage > 0)) {
                    if (stage < ((maxStages - maxStages * flinchPercent)+(maxStages * flinchPercent)/2)) {
                        JavaDrawer.enemyAnimationOffsetX = JavaDrawer.enemyAnimationOffsetX + flichSpeed;
                    } else if (stage > ((maxStages - maxStages * flinchPercent)+(maxStages * flinchPercent)/2)) {
                        JavaDrawer.enemyAnimationOffsetX = JavaDrawer.enemyAnimationOffsetX - flichSpeed;
                    }
                }
            }

        }
    }

    @Override
    public void endAnimation() {
        animate = false;
        JavaDrawer.pFlinch = false;
        JavaDrawer.eFlinch = false;
        GameLoop.finishedAttack();
    }

}
