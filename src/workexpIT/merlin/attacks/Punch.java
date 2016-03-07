package workexpIT.merlin.attacks;

import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.AttackAnimator;
import workexpIT.merlin.graphics.JavaDrawer;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Punch extends  Attack{

    public Punch() {
        super(
                0,
                0,
                DamageType.NORMAL,
                10,
                0,
                "When magic spells aren't good enough, just give 'em a good ol' fashion fist fight"
        );
        texture = JavaDrawer.scale(texture,0.25,0.25);
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
        return (AttackAnimator.AnimationType.animationType.TOWARDS_ENEMY);
    }
    public AttackAnimator.AnimationType getEnemyAniType() {
        return (AttackAnimator.AnimationType.animationType.TOWARDS_PLAYER);
    }
}
