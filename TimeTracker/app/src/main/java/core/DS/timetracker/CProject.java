/**
 * CProject: Class representing the projects of the application.
 * Projects can contain other projects and tasks (See CTask).
 * This is done by making use of the Composite pattern (See CActivity).
 */

package core.ds.TimeTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

public class CProject extends CActivity {

    public CProject(final String name, final String description) {
        super(name, description);
        logger.info("Project " + name + " created: " + description);
    }

    @Override
    public void Accept(final CVisitor visitor) {
        /*
        * Accept: Function used to implement the visitor pattern. It receives the visitor as parameter
        * to the send itself to the visitor for this class
        * @args visitor: Visitor variable that implements the desired functionality for this class */
        visitor.visitProject(this); // Visit this project
        if( visitor.isForwarded() ) { // If the visitor allows, send to children
            Set<String> keys = m_activities.keySet(); // Set of keys to iterate over
            for (String key : keys) { // For each key in keys
                m_activities.get(key).Accept(visitor); // Let the visitor go to the child activities
            }
        }
    }

    /** Gets total time of all contained activities.
     * @return total: Total number of combined milliseconds of all activities */
    @Override
    public long getTotalTime() {
        long total = 0; // Total time to be returned
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over

        for (String key: keys) { // For each key in keys
            CActivity activity = m_activities.get(key); // Get the activity for the given key
            total = total + activity.getTotalTime(); // Update the total time
        }
        return total;
    }

    @Override
    public long getTotalTimeWithin(final long start, final long end) {
        long total = 0; // Total time to be returned
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over

        for (String key: keys) { // For each key in keys
            CActivity activity = m_activities.get(key); // Get the activity for the given key
            total = total + activity.getTotalTimeWithin(start, end); // Update the total time
        }
        return total;
    }

    public void appendActivity(final CActivity activity) {
        activity.setProjectParentName(this.getName());
        m_activities.put(activity.getName(), activity);
    }

    public CActivity getActivity(final String id) { // Get activity with id
        return m_activities.get(id);
    }

    /** trackTaskStart: Gets the task with id and begins the tracking of
     * the activity.
     * @param id Id of the desired task */
    public void trackTaskStart(final String id) {
        try {
            CTask task = (CTask) m_activities.get(id); // Get task to track
            task.trackTaskStart(); // Start tracking task with id
        } catch (ClassCastException e) {
            logger.error("The given id does not belong to a Task");
            e.printStackTrace();
        }
    }

    /** trackTaskStop: Gets the task with id and stops the tracking of the activity
     * @param id Id of the desired task */
    public void trackTaskStop(final String id) {
        try {
            CTask task = (CTask) m_activities.get(id);  // Get task to stop
            task.trackTaskStop(); // Stop tracking task with id
        } catch (ClassCastException e) {
            logger.error("The given id does not belong to a Task");
            e.printStackTrace();
        }
        m_endTime = CClock.getInstance().getTime(); // Set end time to the current time of the Clock
    }

    // TODO Fix this two functions with a visitor or something different.
    /**
     * Iterate over all intervals and return the highest m_currentTime */
    @Override
    public long getCurrentTime() {
        long currentTime = 0;
        Set<String> keys = m_activities.keySet(); // Set of keys of the hashtable
        for (String key: keys) { // For each key in the set
            long time = m_activities.get(key).getCurrentTime(); // Get the element from m_intervals and get current time
            if (time > currentTime) {
                currentTime = time;
            }
        }
        return currentTime;
    }

    /**
     * Iterate over all intervals and return the lowest m_startTime
     */
    @Override
    public long getStartTime() {
        Set<String> keys = m_activities.keySet(); // Set of keys of the hashtable
        Vector<Long> times = new Vector<>();
        for (String key: keys) { // For each key in the set
            long time = m_activities.get(key).getStartTime(); // Get the element from m_intervals and get start time
            if (time > 0) { // If is not 0 add it to vector times
                times.add(time);
            }
        }

        try {
            m_startTime = Collections.min(times); // Get the lowest recorded starting time
        } catch (NoSuchElementException e) {
            m_startTime = 0;
        }
        return m_startTime;
    }

    /* Properties */
    private boolean root = true;
    private Hashtable<String, CActivity> m_activities = new Hashtable<>();
    static Logger logger = LoggerFactory.getLogger("TimeTracker.CActivity.CProject");

}
