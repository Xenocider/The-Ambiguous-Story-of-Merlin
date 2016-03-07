package workexpIT.merlin.attacks;

import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.JavaDrawer;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Bore extends Attack{
    public Bore() {
        super(
                0,
                12,
                DamageType.FIRE,
                20,
                0,
                "Shoots a ball of fire at the opponent for a basic amount of damage"
        );
    }
}
