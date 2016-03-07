package workexpIT.merlin.attacks;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.AttackAnimator;
import workexpIT.merlin.graphics.JavaDrawer;

/**
 * Created by ict11 on 2016-03-02.
 */
public class LifeBend extends Attack{
    public LifeBend() {
        super(
                0,
                -50,
                DamageType.FIRE,
                0,
                15,
                "Shoots a ball of fire at the opponent for a basic amount of damage"
        );
    }
    public int getPlayerX() {
        return (JavaDrawer.playerX + WorldData.getPlayer().battleSprite.getWidth()/2);
    }
    public int getPlayerY() {
        return (JavaDrawer.playerY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/4);
    }
    public int getEnemyX() {
        return (JavaDrawer.enemyX);
    }
    public int getEnemyY() {
        return (JavaDrawer.enemyY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/2);
    }
    public AttackAnimator.AnimationType getPlayerAniType() {
        return (AttackAnimator.AnimationType.animationType.UP);
    }
    public AttackAnimator.AnimationType getEnemyAniType() {
        return (AttackAnimator.AnimationType.animationType.UP);
    }
}
