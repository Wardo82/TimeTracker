package core.DS.timetracker;

import java.io.*;
import java.util.Calendar;
import java.util.Hashtable;

public class CObjectSaver {
    /* CObjectSaver: Object used to store information for persistence. */
/* Methods */
    public static boolean save(Hashtable<String, CActivity> pactivities, String pdestination) {
        boolean saved = false;

        switch (pdestination) {
            case "Serial":
                saved = saveAsSerial(pactivities);
                break;
            case "Database":
                saved = saveToDB(); // TODO
                break;
        }
        return saved;
    }

    private static boolean saveAsSerial(Hashtable<String, CActivity> pactivities) {
        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/employee.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(pactivities);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/activities.ser");
            return true;
        } catch (IOException i) {
            i.printStackTrace();
            return false;
        }
    }

    private static boolean saveToDB() {
        return true;
    }
}
