package no.krekle.abs.abs.driver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krekle on 02/03/16.
 */
public class DriveLog {

    //@SerializedName("custom_id")
    private int userID = 1;
    private int sessionID;
    private List<DriveInstance> logs;

    public DriveLog(int id) {
        this.sessionID = id;
        logs = new ArrayList<DriveInstance>();
    }

    public void addLog(DriveInstance log) {
        logs.add(log);
    }

}
