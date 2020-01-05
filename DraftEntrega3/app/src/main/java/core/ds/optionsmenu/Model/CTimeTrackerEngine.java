package core.ds.optionsmenu.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Set;

/**
 * Singleton class used to handle all activities and connections between the
 * the user and logic of the program, the Object Saver class and the clock.
 * The Singleton pattern was used so that there is only one engine running
 * per application.
 * The Engine is responsible for the output to the user.
 */
public final class CTimeTrackerEngine
        implements java.io.Serializable, PropertyChangeListener {
    /**
     * Public method used to access the only Instance of
     * <code>CTimeTrackerEngine</code> that should run in the program.
     * @return Engine only instance of the application */
    public static CTimeTrackerEngine getInstance() {
        if (m_ourInstance == null) { // If there is no instance
            m_ourInstance = new CTimeTrackerEngine(); // Create one
        }
        return m_ourInstance;
    }

    /** Implements <code>PropertyChangeListener</code> interface to send
     * the <code>CVisitorPrint</code> to all activities to check their status
     * from terminal.
     * @param evt The event that occurred to which the engine was listening
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("Counter")) { // ask if it is the Counter
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


    /** Adds a new CActivity element to activities.
     * @param id ID of the activity to be appended
     * @param activity Activity element of type CActivity */
    public void addActivity(final String id, final CActivity activity) {
        m_activities.put(id, activity);
    }

    /** Removes an activity from activities dictionary.
     * @param id ID of the activity to be removed */
    public void removeActivity(final String id) {
        m_activities.remove(id);
    }

    public CActivity getActivity(final String id) { return m_activities.get(id); } // Get activity with id
    public Hashtable<String, CActivity> getActivities() { return m_activities; } // Get all activities

    /** Set activities from Hashtable. Useful when deserialize function
     * is called.
     * @param activities List of predefined activities. */
    public void setActivities(final Hashtable<String, CActivity> activities) {
        m_activities = activities;
    }

    /**
     * Sets the unit of time at which the Clock and, hence the system,
     * will operate.
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

    /** Loads previous session of time tracking depending on the desired
     * storage.
     * */
    public void load() {
        m_objectSaver.load("Serial");
    }

    /**
     * Generates a report of given type sent as parameter.
     * @param reporter The visitor that traverses the tree and generates
     *                 the report*/
    public void generateReport(final CVisitorReporter reporter) {
        Set<String> keys = m_activities.keySet();
        for (String key: keys) {
            CActivity activity = m_activities.get(key);
            // The visitor is passed and the corresponding function is executed
            activity.Accept(reporter);
            // This reporter is sent by the engine, not the parent Project
            reporter.setParentName(null);
        }
        reporter.generateReport();
    }

    /** Private constructor for Singleton pattern. */
    private CTimeTrackerEngine() { }
    private static CTimeTrackerEngine m_ourInstance = null;
    // Object used for persistence (i.e. Serializing and DB handling)
    private CObjectSaver m_objectSaver = new CObjectSaver();
    // Set of projects and activities the user has created.
    private Hashtable<String, CActivity> m_activities = new Hashtable<>();
    private long m_timeUnit; // Time step in milliseconds
}
