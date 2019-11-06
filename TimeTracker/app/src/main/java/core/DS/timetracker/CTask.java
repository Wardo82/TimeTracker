package core.DS.timetracker;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;


public class CTask extends CActivity {
    /**
     * CTask: Class representing the tasks of the application. Tasks are the only type of activity that
     * whose time can be tracked. This class contains an array of intervals (See CInterval) that are used
     * to track the time.
     * **/

    public CTask() {}
    public CTask(String pname, String pdescription) { // Constructor with parameters
        super(pname, pdescription); // Calls superclass constructor
    }

/* Methods */
    @Override
    public void Accept(CVisitor visitor) { visitor.visitTask(this); } // Visit this activity

    // Getters
    public CInterval getInterval(String id) { return m_intervals.get(id); }

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

    /* Interval handling methods */
    public void appendInterval(CInterval interval) {
        // Create one interval object and append it to the intervals list
        m_intervals.put(interval.getName(), interval);
    }

    public void trackTaskStart() {
        CInterval interval = new CInterval(String.valueOf(m_intervals.size()), "");
        interval.start();
        m_intervals.put(interval.getName(), interval);

        if (m_startTime == 0) { // If no other interval has been started the m_start time is 0
            m_startTime = CClock.getInstance().getTime(); // Ask clock for current time
        }
    }

    public void trackTaskStop() {
        CInterval interval = m_intervals.get(String.valueOf(m_intervals.size()-1)); // Get the last interval
        interval.end(); // Stop timing the interval
        m_endTime = CClock.getInstance().getTime();
    }

/* Properties */
    private Map<String, CInterval> m_intervals = new LinkedHashMap<>();
}
