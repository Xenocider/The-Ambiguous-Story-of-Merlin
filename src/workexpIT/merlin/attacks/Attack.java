package workexpIT.merlin.attacks;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.data.ImageReader;

import java.awt.image.BufferedImage;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Attack {

    public enum DamageType {NORMAL,FIRE,WATER,EARTH,AIR};

    public int manaDamage;
    public int manaCost;
    public int enemyDamage;
    public int selfDamage;
    public DamageType damageType;
    public String description = "default description";
    public BufferedImage texture;

    public Attack(int dmana, int mana, DamageType dt,int enemyD, int selfD, String desc) {
        manaDamage = dmana;
        manaCost = mana;
        damageType = dt;
        enemyDamage = enemyD;
        selfDamage = selfD;
        description = desc;
        texture = ImageReader.loadImage("resources/graphics/attacks/"+this.getClass().getSimpleName()+".png");
    }


}
