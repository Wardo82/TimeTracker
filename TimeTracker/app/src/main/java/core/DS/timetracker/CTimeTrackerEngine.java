package core.ds.TimeTracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Set;

public class CTimeTrackerEngine implements java.io.Serializable, PropertyChangeListener {
    /**
    * CTimeTrackerEngine: Singleton class used to handle all activities and connections between the
    * the user and logic of the program, the Object Saver class and the clock.
    * The Singleton pattern was used so that there is only one engine running per application.
    * **/

/* Methods */
    public static CTimeTrackerEngine getInstance() {
        /* getInstance: Method used to get the CTimeTrackerEngine singleton object. */
        if(m_ourInstance == null) { // If there is no instance
            m_ourInstance = new CTimeTrackerEngine(); // Create one
        }
        return m_ourInstance;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /* This function is needed for Listeners to the clock. Each time the Clock does a "tic" this function is called.
        * It will be executed each change of the clock counter. The Engine is responsible for the output
        * to the user. In this function the printVisitor object is sent to all activities to check their status from
        * terminal
        * @arg evt: The event that occurred to which the engine was listening
        */
        if(evt.getPropertyName().equals("Counter")) {// For the case of many Observables, ask if it is the Counter
            // Preliminary variables
            CVisitorPrint printVisitor = new CVisitorPrint(); // Visitor object that prints to console
            Set<String> keys = m_activities.keySet(); // Set of keys to iterate over

            for (String key: keys) { // For each key in keys
                CActivity activity = m_activities.get(key); // Get the activity for the given key
                System.out.println("===============================================");
                activity.Accept(printVisitor); // The visitor is passed and the corresponding function is executed
            }
        }
    }

    public void addActivity(String id, CActivity activity) {
        /* addActivity: Adds a new CActivity element to activities.
        * @arg id: ID of the activity to be appended
        * @arg activity: Activity element of type CActivity */
        m_activities.put(id, activity);
    }

    public void removeActivity(String id) {
        /* removeActivity: Removes an activity from activities dictionary
         * @arg id: ID of the activity to be removed */
        m_activities.remove(id);
    }

    public CActivity getActivity(final String id) { return m_activities.get(id); } // Get activity with id
    public Hashtable<String, CActivity> getActivities() { return m_activities; } // Get all activities

    // Set activities from Hashtable. Useful when deserialize function is called
    public void setActivities(final Hashtable<String, CActivity> activities) {
        m_activities = activities;
    }

    /**
     * Sets the unit of time at which the Clock and, hence the system,
     * will operate
     * @param timeUnit Rate of steps, set in milliseconds. */
    public void setTimeUnit(final long timeUnit) {
        m_timeUnit = timeUnit;
        CClock.getInstance().setTimeUnit(m_timeUnit);
    }

    /** Stores the information held in m_activities on the desired destination
     * using the ObjectSaver. */
    public void save() {
        m_objectSaver.save("Serial");
    }

    public void load() {
        /* load: Loads previous session of time tracking depending on the desired storage. */
        m_objectSaver.load("Serial");
    }

    public void generateReport(CVisitorReporter reporter) {
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over
        for (String key: keys) { // For each key in keys
            CActivity activity = m_activities.get(key); // Get the activity for the given key
            activity.Accept(reporter); // The visitor is passed and the corresponding function is executed
            reporter.setParentName(null);
        }
        reporter.generateReport();
    }

    /* Properties */
    private CTimeTrackerEngine() {} // Private constructor of a Singleton pattern
    private static CTimeTrackerEngine m_ourInstance = null;
    private CObjectSaver m_objectSaver = new CObjectSaver(); // Object used for persistence (i.e. Serializing and DB handling)
    private Hashtable<String, CActivity> m_activities = new Hashtable<>(); // Set of projects and activities the user has created.
    private long m_timeUnit; // Time step in milliseconds
}
