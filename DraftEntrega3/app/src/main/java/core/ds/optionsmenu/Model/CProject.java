package core.ds.optionsmenu.Model;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import static android.content.ContentValues.TAG;

/**
 * Class representing the projects of the application.
 * Projects can contain other projects and tasks (See CTask).
 * This is done by making use of the Composite pattern (See CActivity).
 */
public class CProject extends CActivity {

    /** Initialize name and description of this activity.
     * @param name The name of the activity.
     * @param description The description of the activity. */
    public CProject(final String name, final String description) {
        super(name, description);
        logger.info("Project {} created: {}", name, description);
    }

    /**
     * Function used to implement the visitor pattern.
     * It receives the visitor as parameter to the send itself to the visitor
     * for this class.
     * @param  visitor Visitor variable that implements the desired
     *                 functionality for this class */
    @Override
    public void Accept(final CVisitor visitor) {
        visitor.visitProject(this); // Visit this project
        if (visitor.isForwarded()) { // If the visitor allows, send to children
            Set<String> keys = m_activities.keySet();
            for (String key : keys) { // For each key in keys
                // Let the visitor go to the child activities
                m_activities.get(key).Accept(visitor);
            }
        }
    }

    /** Gets total time of all contained activities.
     * @return Total number of combined milliseconds of all activities */
    @Override
    public long getTotalTime() {
        long total = 0; // Total time to be returned
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over

        for (String key: keys) { // For each key in keys
            CActivity activity = m_activities.get(key); // Get the activity for the given key
            total = total + activity.getTotalTime(); // Update the total time
        }
        m_endTime = m_startTime + total;
        return total;
    }

    /** Gets total time of all contained activities within
     * bounds (start, end).
     * @param start Start of the period of interest
     * @param end End of the period of interest
     * @return Total number of combined milliseconds of all activities */
    @Override
    public long getTotalTimeWithin(final long start, final long end) {
        long total = 0; // Total time to be returned
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over

        for (String key: keys) {
            CActivity activity = m_activities.get(key);
            total = total + activity.getTotalTimeWithin(start, end);
        }
        return total;
    }

    public void appendActivity(final CActivity activity) {
        activity.setProjectParent(this);
        m_activities.put(activity.getName(), activity);
    }

    public CActivity getActivity(final String id) {return m_activities.get(id); }
    public Collection getChildren() {return m_activities.values(); }
    public Map<String, CActivity> getActivities() {return m_activities; }

    /** Gets the task with id and begins the tracking of the activity.
     * @param id Id of the desired task */
    public void trackTaskStart(final String id) {
        try {
            CTask task = (CTask) m_activities.get(id);
            try {
                task.trackTaskStart(); // Start tracking task with id
            } catch (Exception e) {
                logger.error("There is another interval running at the moment", e);
            }
        } catch (ClassCastException e) {
            logger.error("Element with id {} does not belong to a Task", id, e);
        }
    }
    /** Gets the task with id and stops the tracking of the activity.
     * @param id Id of the desired task.
     */
    public void trackTaskStop(final String id) {
        try {
            CTask task = (CTask) m_activities.get(id);  // Get task to stop
            try {
                // Stop tracking task with id
                task.trackTaskStop();
            } catch (Exception e) {
                logger.error("Empty list of intervals", e);
            }
        } catch (ClassCastException e) {
            logger.error("Element with id {} does not belong to a Task", id, e);
        }
        // Set end time to the current time of the Clock
        m_endTime = m_currentTime;
    }

    // TODO Fix this two functions with a Visitor or something different.
    @Override
    public long getCurrentTime() {
        logger.warn("getCurrentTime() might be deprecated in the future.");
        long currentTime = 0;
        // Set of keys of the hashtable
        Set<String> keys = m_activities.keySet();
        // For each key in the set
        for (String key: keys) {
            // Get the element from m_intervals and get current time
            long time = m_activities.get(key)
                    .getCurrentTime();
            if (time > currentTime) {
                currentTime = time;
            }
        }
        m_endTime = m_currentTime;
        return currentTime;
    }

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
            // Get the lowest recorded starting time.
            m_startTime = Collections.min(times);
        } catch (NoSuchElementException e) {
            logger.warn("Project {} has no activities", this.getName());
            m_startTime = 0;
        }
        return m_startTime;
    }
    /**
     * Gets the time of the last element of the hashtable.
     * @return The highest ending time of the children.
     */
    @Override
    public long getEndTime() {
        Set<String> keys = m_activities.keySet(); // Set of keys of the hashmap
        Vector<Long> times = new Vector<>();
        // For each key in the set
        for (String key: keys) {
            // Get the element from m_intervals and get start time
            long time = m_activities.get(key).getEndTime();
            if (time > 0) { // If is not 0 add it to vector times
                times.add(time);
            }
        }

        try {
            // Get the highest recorded ending time.
            m_endTime = Collections.max(times);
        } catch (NoSuchElementException e) {
            Log.w(TAG, "Project "
                    + this.getName()
                    + " has no activities");
            m_endTime = 0;
        }
        return m_endTime;
    }

    /**
     * Erases an child element of project.
     * @param key
     */
    @Override
    public void eraseElement(final String key) {
        m_activities.remove(key);
    }

    private Map<String, CActivity> m_activities = new LinkedHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(CProject.class);
}
