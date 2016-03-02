package workexpIT.merlin.attacks;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Fireball extends Attack{
    public Fireball() {
        super(
                12,
                DamageType.FIRE,
                20,
                0,
                "Shoots a ball of fire at the opponent for a basic amount of damage"
        );
    }
}
