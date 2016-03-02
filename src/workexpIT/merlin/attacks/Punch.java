package workexpIT.merlin.attacks;

/**
 * Created by ict11 on 2016-03-02.
 */
public class Punch extends  Attack{

    public Punch() {
        super(
                0,
                DamageType.NORMAL,
                10,
                0,
                "When magic spells aren't good enough, just give 'em a good ol' fashion fist fight"
        );
    }
}
