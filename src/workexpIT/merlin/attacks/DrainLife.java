package workexpIT.merlin.attacks;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.AttackAnimator;
import workexpIT.merlin.graphics.JavaDrawer;

/**
 * Created by ict11 on 2016-03-02.
 */
public class DrainLife extends Attack{
    public DrainLife() {
        super(
                0,
                30,
                DamageType.NORMAL,
                20,
                -10,
                "Shoots a ball of fire at the opponent for a basic amount of damage"
        );
    }
    public int getEnemyX() {
        return (JavaDrawer.playerX + WorldData.getPlayer().battleSprite.getWidth()/2);
    }
    public int getEnemyY() {
        return (JavaDrawer.playerY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/4);
    }
    public int getPlayerX() {
        return (JavaDrawer.enemyX);
    }
    public int getPlayerY() {
        return (JavaDrawer.enemyY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/2);
    }
    public AttackAnimator.AnimationType getEnemyAniType() {
        return (AttackAnimator.AnimationType.animationType.TOWARDS_ENEMY);
    }
    public AttackAnimator.AnimationType getPlayerAniType() {
        return (AttackAnimator.AnimationType.animationType.TOWARDS_PLAYER);
    }
}
