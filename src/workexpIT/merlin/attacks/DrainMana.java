package workexpIT.merlin.attacks;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;

/**
 * Created by ict11 on 2016-03-02.
 */
public class DrainMana extends Attack{
    public DrainMana() {
        super(
                20,
                -10,
                DamageType.NORMAL,
                0,
                0,
                "Shoots a ball of fire at the opponent for a basic amount of damage"
        );
    }
    @Override
    public void runEnemyAnimation() {
        Attack.start(
                this.texture,
                JavaDrawer.playerX + WorldData.getPlayer().battleSprite.getWidth()/2,
                JavaDrawer.playerY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/4,
                animationType.TOWARDS_ENEMY);
    }
    @Override
    public void runPlayerAnimation() {
        Attack.start(
                this.texture,
                JavaDrawer.enemyX,
                JavaDrawer.enemyY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/2,
                animationType.TOWARDS_PLAYER);
    }
}
