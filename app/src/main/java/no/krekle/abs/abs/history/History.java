package no.krekle.abs.abs.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by krekle on 17/04/16.
 */
public class History {
    private static History ourInstance = new History();
    private ArrayList<HashMap> history;

    public static History getInstance() {
        return ourInstance;
    }

    private History() {
    }

    public void addHistory(String msg) {
        if (history == null) {
            history = new ArrayList<>();
        }
        HashMap map = new HashMap();
        map.put("DATE", new Date());
        map.put("MSG", msg);
        history.add(map);
    }

    public ArrayList<HashMap> getHistory() {
        return history;
    }
}
