package core.ds.optionsmenu.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

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
     * Invariant method
     *
     */
    protected boolean invariant() {
        boolean correctTask = true;
        if (m_currentTime < m_startTime || m_currentTime > m_endTime) {
            correctTask = false;
            logger.error("current time wrong");
        }
        if (m_endTime < 0 || m_currentTime < 0 || m_startTime < 0) {
            correctTask = false;
            logger.error("time less than 0");
        }
        if (m_endTime < m_startTime) {
            correctTask = false;
            logger.error("end time bigger than start time");
        }
        if (m_intervals == null) {
            correctTask = false;
            logger.error("interval equals null");
        }
        return correctTask;
    }

    /**
     * Function used to implement the visitor pattern.
     * It receives the visitor as parameter to the send itself to the visitor
     * for this class.
     * @param  visitor Visitor variable that implements the desired
     *                 functionality for this class */
    @Override
    public void Accept(final CVisitor visitor) {
        assert invariant() : "Invalid Time";
        visitor.visitTask(this);
        if (visitor.isForwarded()) { // If the visitor allows, send to children
            Set<String> keys = m_intervals.keySet(); // Set of keys to iterate over
            for (String key : keys) { // For each key in keys
                m_intervals.get(key).Accept(visitor); // Let the visitor go to the child activities
            }
        }
    }

    public CInterval getInterval(final String id) {return m_intervals.get(id); }

    /** Gets total time of all contained intervals.
     * @return Total number of combined milliseconds of all intervals */
    @Override
    public long getTotalTime() {
        // Iterate over all intervals and return their combined time in milliseconds.
        assert invariant() : "Invalid Time";
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
        assert invariant() : "Invalid Time";
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException("Start and End time must be bigger than 0. And End time must be bigger than Start Time");
        }
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
        assert invariant() : "Invalid Time";
        long currentTime = 0;
        Set<String> keys = m_intervals.keySet(); // Set of keys of the hashtable
        for (String key: keys) { // For each key in the set
            long time = m_intervals.get(key).getCurrentTime(); // Get the element from m_intervals and get current time
            if (time > currentTime) {
                currentTime = time;
            }
        }
        m_endTime = m_currentTime;

        return currentTime;
    }

    @Override
    public Collection getChildren() {
        return m_intervals.values();
    }

    public void appendInterval(final CInterval interval) {
        // Create one interval object and append it to the intervals list
        assert invariant() : "Invalid Time";
        interval.setTaskParentName(this.getName());
        interval.setProjectName(this.m_projectParent.getName());
        m_intervals.put(interval.getName(), interval);
    }

    /**
     * Starts Task time, a new interval is created.
     *
     */
    public void trackTaskStart() throws Exception {

        assert invariant() : "Invalid Time";

        if (!m_intervals.isEmpty()
                && m_intervals.get(String.valueOf(m_intervals.size() - 1)).isRunning()) {
            throw new RejectedExecutionException("There is another interval running at the moment");
        }

        CInterval interval = new CInterval(
                String.valueOf(m_intervals.size()), "");

        interval.start();
        interval.setTaskParentName(this.getName());
        interval.setProjectName(m_projectParent.getName());
        m_intervals.put(interval.getName(), interval);

        if (m_startTime == 0) { // If no other interval has been started the m_start time is 0
            m_startTime = CClock.getInstance().getTime(); // Ask clock for current time
        }

        m_currentTime = CClock.getInstance().getTime();
        m_endTime = m_currentTime;
        this.isTracked(true);
        assert (interval.getProjectName() != null)
                : "Project name must not be null";
        assert (m_startTime != 0)
                : "Start Time must be bigger than 0 at this point ";
    }

    /**
     * Stop Task, end of the interval.
     * @throws Exception if the interval lists is empty
     */
    public void trackTaskStop() throws Exception {

        assert invariant() : "Invalid Time";
        if (m_intervals.isEmpty()) {
            throw new IndexOutOfBoundsException("Empty list of intervals");
        }
        CInterval interval = m_intervals.get(String.valueOf(m_intervals.size() - 1)); // Get the last interval
        interval.end(); // Stop timing the interval
        m_endTime = CClock.getInstance().getTime();
        this.isTracked(false);
        assert (m_endTime != 0) : "End Time must be bigger than 0 at this point ";
    }
    /**
     * Gets the end time of the last interval
     * @return
     */
    @Override
    public long getEndTime() {
        // Get the last interval
        CInterval interval;
        long end = 0;
        try {
            interval = m_intervals.get(
                    String.valueOf(m_intervals.size() - 1));
            end = interval.getEndTime();
        } catch (Exception e) {
            end = 0;
        }
        return end;
    }

    /**
     * Erases an interval from task.
     * @param key Key of interval.
     */
    @Override
    public void eraseElement(final String key) {
        m_intervals.remove(key);
    }

    private Map<String, CInterval> m_intervals = new LinkedHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(CTask.class);
}
