package workexpIT.merlin;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by ict11 on 2016-02-03.
 */
public class Output {

    public static void write(String string) {
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        System.out.println("[" + time + "] " +string);
    }
    public static void error(String string) {
        write("[ERROR]   " + string);
    }
}
