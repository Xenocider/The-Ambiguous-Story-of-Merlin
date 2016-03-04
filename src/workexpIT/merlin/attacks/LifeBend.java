package workexpIT.merlin.attacks;

import workexpIT.merlin.data.WorldData;
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
    @Override
    public void runPlayerAnimation() {
        Attack.start(
                this.texture,
                JavaDrawer.playerX + WorldData.getPlayer().battleSprite.getWidth()/2,
                JavaDrawer.playerY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/4,
                animationType.UP);
    }
    @Override
    public void runEnemyAnimation() {
        Attack.start(
                this.texture,
                JavaDrawer.enemyX,
                JavaDrawer.enemyY + WorldData.getPlayer().battleSprite.getHeight()/4 - texture.getHeight()/2,
                animationType.UP);
    }
}
