package no.krekle.abs.abs.driver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by krekle on 02/03/16.
 */
public class DriveInstance {

    private String timeStamp;
    private int speed; // ms
    //private double acceleration;
    // TODO ---- MORE FIELDS ----

    public DriveInstance(int currentSpeed) {
        timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        speed = currentSpeed;
    }

}
