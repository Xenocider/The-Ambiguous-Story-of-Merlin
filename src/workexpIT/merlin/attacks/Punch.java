package workexpIT.merlin.attacks;

import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
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
    @Override
    public  void runPlayerAnimation() {
        Attack.start(
                this.texture,
                JavaDrawer.playerX + WorldData.getPlayer().battleSprite.getWidth()/2,
                //Normally should be /2 not /4
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