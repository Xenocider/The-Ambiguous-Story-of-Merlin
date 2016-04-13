package workexpIT.merlin;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by ict11 on 2016-02-03.
 */
public class Output {

    private static Timestamp recordStartTime;
    public static void recordStart() {
        recordStartTime = new Timestamp(new Date().getTime());
    }
    public static long recordEnd() {
        Timestamp recordEndTime = new Timestamp(new Date().getTime());
        long time = recordEndTime.getTime() - recordStartTime.getTime();
        return time;
    }

    public static void write(String string) {
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        //System.out.println("[" + time + "] " +string);
    }
    public static void error(String string) {
        write("[ERROR]   " + string);
    }



    public static void log(String string) {
        //System.out.println(string);
    }
}
