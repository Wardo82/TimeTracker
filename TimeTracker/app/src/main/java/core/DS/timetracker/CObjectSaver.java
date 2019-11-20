package core.ds.TimeTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Hashtable;

/**
 * Object used to store information for persistence.
 * Is used by the TimeTrackerEngine to save the activities in the
 * desired destination. */
public class CObjectSaver implements Serializable {

   /** Generic public function that calls the desired method of persistence.
    * @param destination Type of persistence mode [String]
    * */
    public void save(final String destination) {
        switch (destination) {
            case "Serial":
                saveAsSerial();
                break;
            case "Database":
                saveToDB(); // TODO
                break;
            default:
                logger.error("Persistence mode {} not implemented.");
                break;
        }
    }

    /** Generic public function that loads the data from de desired
     * destination.
     * @param destination Type of persistence mode [String] */
    public void load(final String destination) {
        switch (destination) {
            case "Serial":
                loadFromSerial();
                break;
            case "Database":
                // loaded = loadFromDB(); // TODO
                break;
            default:
                logger.error("Persistence mode {} not implemented.");
        }
    }

    private void saveAsSerial() {
        try {
            FileOutputStream fileOut = new FileOutputStream(m_localDestination);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(CTimeTrackerEngine.getInstance().getActivities());
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in "
                    + m_localDestination);
        } catch (IOException i) {
            logger.error("IOException in ObjectDataSaver:");
            i.printStackTrace();
        }
    }

    private boolean saveToDB() {
        return true;
    }

    private void loadFromSerial() {

        try {
            FileInputStream fis = new FileInputStream(m_localDestination);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Hashtable<String, CActivity> activities = (Hashtable<String, CActivity>) ois.readObject();
            CTimeTrackerEngine.getInstance().setActivities(activities);
            ois.close();
            fis.close();
            System.out.printf("Serialized data loaded from "
                    + m_localDestination + "\n");

        } catch (Exception e) {
            logger.error("IOException in ObjectDataSaver:");
            e.printStackTrace();
        }

    }

    private String m_localDestination = "TimeTracker.ser";
    static Logger logger = LoggerFactory.getLogger(CObjectSaver.class);

}
