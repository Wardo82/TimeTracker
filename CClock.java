package core.DS.timetracker;

import java.util.Date;
import java.util.Calendar;

public class CClock {

    private static CClock Instance = null;

    public static CClock getInstance() {
        if (Instance == null) {
            Instance = new CClock();
        }
        return Instance;
    }

    public Date getTime() {
        return Calendar.getInstance().getTime();
    }

    private CClock() {}

}
