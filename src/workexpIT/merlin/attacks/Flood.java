package workexpIT.merlin.attacks;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Flood extends Attack{
    public Flood() {
        super(
                0,
                20,
                DamageType.WATER,
                30,
                0,
                "Floods the surrounding area"
        );
        texture = JavaDrawer.scale(texture,2,1);
    }
    @Override
    public void runPlayerAnimation() {
        Attack.start(
                this.texture,
                0,
                JavaDrawer.frame.getHeight()-texture.getHeight(),
                animationType.UP);
    }
    @Override
    public void runEnemyAnimation() {
        Attack.start(
                this.texture,
                0,
                JavaDrawer.frame.getHeight()-texture.getHeight(),
                animationType.UP);
    }
}
