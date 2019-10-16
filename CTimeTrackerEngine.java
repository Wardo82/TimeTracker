package core.DS.timetracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Set;

public class CTimeTrackerEngine implements java.io.Serializable, PropertyChangeListener {
    /*
    * CTimeTrackerEngine: Singleton class used to handle all activities and connections between the
    * interface, the Object Saver class (for persistence in general) and the clock.
    * */

/* Methods */
    public static CTimeTrackerEngine getInstance() {
        /* getInstace: Method used to get the CTimeTrackerEngine singleton object. */
        if(m_ourInstance == null) { // If there is no instance
            m_ourInstance = new CTimeTrackerEngine(); // Create one
        }
        return m_ourInstance;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("Counter")) {// For the case of many Observables, ask if it is the Counter
            // Preliminary variables
            CPrintVisitor printVisitor = new CPrintVisitor();
            Set<String> keys;

            keys = m_activities.keySet(); // Set of keys to iterate over

            for (String key: keys) { // For each key in keys
                CActivity activity = m_activities.get(key); // Get the activity for the given key
                activity.Accept(printVisitor);
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

    public CActivity getActivity(String id) { return m_activities.get(id); }

    public void setTimeUnit(long timeUnit) {
        m_timeUnit = timeUnit;
        CClock.getInstance().setTimeUnit(timeUnit);
    }

    public boolean save() {
        /* save: Stores the information held in m_activities on the desired destination using
        * the ObjectSaver member. */
        return m_objectSaver.save(m_activities, "Serial");
    }

/* Properties */
    private CTimeTrackerEngine() {} // Private constructor of a Singleton pattern
    private static CTimeTrackerEngine m_ourInstance = null;
    private CObjectSaver m_objectSaver = new CObjectSaver(); // Object used for persistence (i.e. Serializing and DB handling)
    private Hashtable<String, CActivity> m_activities = new Hashtable<>(); // Set of projects and activities the user has created.
    private long m_timeUnit;
}
