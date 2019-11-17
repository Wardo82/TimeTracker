package core.ds.TimeTracker;

import java.io.*;
import java.util.Hashtable;

public class CObjectSaver implements Serializable{
    /** CObjectSaver: Object used to store information for persistence. Is used by the TimeTrackerEngine
    * to save the activities in the desired destination
    * **/
/* Methods */
    public void save(String pdestination) {
        /* save: generic public function that calls the desired method of persistence.
        * @arg pdestination: Type of persistence mode [String]
        * */
        switch (pdestination) {
            case "Serial":
                saveAsSerial();
                break;
            case "Database":
                saveToDB(); // TODO
                break;
        }
    }

    public void load(String destination) {
        /* load: generic public function that calls the desired method for persistence.
         * @arg pdestination: Type of persistence mode [String]
         * */
        switch (destination) {
            case "Serial":
                loadFromSerial();
                break;
            case "Database":
                // loaded = loadFromDB(); // TODO
                break;
        }
    }

    private void saveAsSerial() {
        try {
            FileOutputStream fileOut = new FileOutputStream(m_localDestination);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject( CTimeTrackerEngine.getInstance().getActivities() );
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in "+m_localDestination);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private boolean saveToDB() {
        return true;
    }

    private void loadFromSerial() {

        try{
            FileInputStream fis = new FileInputStream(m_localDestination);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Hashtable<String, CActivity> activities = (Hashtable<String, CActivity>) ois.readObject();
            CTimeTrackerEngine.getInstance().setActivities(activities);
            ois.close();
            fis.close();
            System.out.printf("Serialized data loaded from "+m_localDestination+"\n");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String m_localDestination = "TimeTracker.ser";
}
