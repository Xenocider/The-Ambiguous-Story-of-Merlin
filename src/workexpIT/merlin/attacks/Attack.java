package workexpIT.merlin.attacks;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Attack {

    public enum DamageType {NORMAL,FIRE,WATER,EARTH,AIR};

    public int manaCost;
    public int enemyDamage;
    public int selfDamage;
    public DamageType damageType;
    public String description = "default description";

    public Attack(int mana,DamageType dt,int enemyD, int selfD, String desc) {
        manaCost = mana;
        damageType = dt;
        enemyDamage = enemyD;
        selfDamage = selfD;
        description = desc;
    }


}
