package workexpIT.merlin.attacks;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;

/**
 * Created by ict11 on 2016-03-02.
 */
public class RockToss extends Attack{
    public RockToss() {
        super(
                0,
                12,
                DamageType.FIRE,
                20,
                0,
                "Shoots a ball of fire at the opponent for a basic amount of damage"
        );
    }
    @Override
    public void runPlayerAnimation() {
        Attack.start(
                this.texture,
                JavaDrawer.playerX + WorldData.getPlayer().battleSprite.getWidth()/2,
                JavaDrawer.playerY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/4,
                animationType.TOWARDS_ENEMY);
    }
    @Override
    public void runEnemyAnimation() {
        Attack.start(
                this.texture,
                JavaDrawer.enemyX,
                JavaDrawer.enemyY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/2,
                animationType.TOWARDS_PLAYER);
    }
}
