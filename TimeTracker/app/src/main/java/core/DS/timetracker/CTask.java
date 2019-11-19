package core.ds.TimeTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Class representing the tasks of the application. Tasks are the
 * only type of activity that whose time can be tracked. This class
 * contains an array of intervals (See CInterval) that are used
 * to track the time.
 */
public class CTask extends CActivity {

    public CTask() { }

    /** Initialize name and description of this activity.
     * @param name The name of the activity.
     * @param description The description of the activity. */
    public CTask(final String name, final String description) {
        super(name, description);
        logger.info("Task " + name + " created: " + description);
    }

    /**
     * Function used to implement the visitor pattern.
     * It receives the visitor as parameter to the send itself to the visitor
     * for this class.
     * @param  visitor Visitor variable that implements the desired
     *                 functionality for this class */
    @Override
    public void Accept(final CVisitor visitor) {
        visitor.visitTask(this);
        if (visitor.isForwarded()) { // If the visitor allows, send to children
            Set<String> keys = m_intervals.keySet(); // Set of keys to iterate over
            for (String key : keys) { // For each key in keys
                m_intervals.get(key).Accept(visitor); // Let the visitor go to the child activities
            }
        }
    }

    public CInterval getInterval(final String id) { return m_intervals.get(id); }

    /** Gets total time of all contained intervals.
     * @return Total number of combined milliseconds of all intervals */
    @Override
    public long getTotalTime() {
        // Iterate over all intervals and return their combined time in milliseconds.
        long total = 0;
        Set<String> keys = m_intervals.keySet(); // Set of keys of the hashtable
        for (String key: keys) { // For each key in the set
            total = total + m_intervals.get(key).getTotalTime(); // Get the element from m_intervals and update the total
        }
        return total;
    }

    /** Gets total time of all contained activities within
     * bounds (start, end).
     * @param start Start of the period of interest
     * @param end End of the period of interest
     * @return Total number of combined milliseconds of all activities */
    @Override
    public long getTotalTimeWithin(final long start, final long end) {
        long total = 0;
        Set<String> keys = m_intervals.keySet(); // Set of keys of the hashtable

        for (String key: keys) { // For each key in the set
            total = total + m_intervals.get(key).getTotalTimeWithin(start, end); // Get the element from m_intervals and update the total
        }
        return total;
    }

    @Override
    public long getCurrentTime() {
        // Iterate over all intervals and return the highest m_currentTime
        long currentTime = 0;
        Set<String> keys = m_intervals.keySet(); // Set of keys of the hashtable
        for (String key: keys) { // For each key in the set
            long time = m_intervals.get(key).getCurrentTime(); // Get the element from m_intervals and get current time
            if (time > currentTime) {
                currentTime = time;
            }
        }
        return currentTime;
    }

    public void appendInterval(final CInterval interval) {
        // Create one interval object and append it to the intervals list
        interval.setTaskParentName(this.getName());
        interval.setProjectName( this.m_projectParentName );
        m_intervals.put(interval.getName(), interval);
    }

    public void trackTaskStart() {
        CInterval interval = new CInterval(
                String.valueOf(m_intervals.size()), "");
        interval.start();
        interval.setTaskParentName(this.getName());
        interval.setProjectName(this.m_projectParentName);
        m_intervals.put(interval.getName(), interval);

        if (m_startTime == 0) { // If no other interval has been started the m_start time is 0
            m_startTime = CClock.getInstance().getTime(); // Ask clock for current time
        }
    }

    public void trackTaskStop() {
        CInterval interval = m_intervals.get(String.valueOf(m_intervals.size() - 1)); // Get the last interval
        interval.end(); // Stop timing the interval
        m_endTime = CClock.getInstance().getTime();
    }

    private Map<String, CInterval> m_intervals = new LinkedHashMap<>();
    static Logger logger = LoggerFactory.getLogger("TimeTracker.CActivity.CTask");

}
