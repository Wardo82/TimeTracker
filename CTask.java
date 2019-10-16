package core.DS.timetracker;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;


public class CTask extends CActivity implements Runnable{

    public CTask(String pname, String pdescription) {
        super(pname, pdescription);
    }

/* Methods */
    @Override
    public void Accept(CVisitor visitor) { visitor.visitTask(this); } // Visit this activity

    @Override
    public void run() {
        trackTaskStart();
        while (m_tracking) {

        }
    }

    /* Getters and setters */
    @Override
    public CActivity getObject(String id) { return m_intervals.get(id); }

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
    @Override
    public void appendObject(CActivity act) {
        // Create one interval object and append it to the intervals list
        try {
            m_intervals.put(act.getName(), (CInterval) act); // TODO: Preguntar si esto está bien
        }catch (ClassCastException e){
            System.out.println("Class should be CInterval");
        }
    }

    public void trackTaskStart() {
        m_tracking = true; // For the thread to be alive
        CInterval interval = new CInterval(String.valueOf(m_intervals.size()), "");
        interval.start();
        CClock.getInstance().addPropertyChangeListener(interval); // Add interval as listener to the Clock
        m_intervals.put(interval.getName(), interval);

        if (m_startTime == 0) { // If no other interval has been started the m_start time is 0
            m_startTime = CClock.getInstance().getTime(); // Ask clock for current time
        }
    }

    public void trackTaskStop() {
        m_tracking = false; // To kill the Thread
        CInterval interval = m_intervals.get(String.valueOf(m_intervals.size()-1)); // Get the last interval
        interval.end(); // Stop timing the interval
        CClock.getInstance().removePropertyChangeListener(interval);
        m_endTime = CClock.getInstance().getTime();
    }

/* Properties */
    private Map<String, CInterval> m_intervals = new LinkedHashMap<>();
    private boolean m_tracking; // Flag to kill the thread
}
