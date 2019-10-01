package core.DS.timetracker;

import java.util.Hashtable;

public class CObjectSaver {
    /* CObjectSaver: Object used to store information for persistence. */
/* Methods */
    public static boolean save(Hashtable pactivities, String pdestination) {
        boolean saved = false;

        switch (pdestination) {
            case "Serial":
                saved = saveAsSerial(); // TODO
                break;
            case "Database":
                saved = saveToDB(); // TODO
                break;
        }
        return saved;
    }

    private static boolean saveAsSerial() {
        return true;
    }

    private static boolean saveToDB() {
        return true;
    }
}
