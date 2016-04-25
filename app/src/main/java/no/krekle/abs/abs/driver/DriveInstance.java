package no.krekle.abs.abs.driver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by krekle on 02/03/16.
 */
public class DriveInstance {

    private String timeStamp;
    private int speed = -1; // ms
    private int rpm = -1;
    private int throttle = -1;
    private int brake = -1;
    // TODO ---- MORE FIELDS ----

    public DriveInstance(int currentSpeed) {
        timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        speed = currentSpeed;
    }

    public DriveInstance() {
        timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public int getSpeed() {
        return speed;
    }

    public double getKmSpeed() {
        return speed * 3.6;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
    }

    public int getBrake() {
        return brake;
    }

    public void setBrake(int brake) {
        this.brake = brake;
    }

    /**
     * Method for checking if this log is complete,
     * if it is filled with the required data
     *
     * @return
     */
    public boolean isComplete() {
        // TODO, add more required data (Settings)
        if (this.speed >= 0) {
            if (this.rpm >= 0) {
                if (this.throttle >= 0) {
                    if (this.brake >= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCompleteSpeedOnly() {
        if(this.speed >= 0) {
            return true;
        }
        return false;
    }
}
