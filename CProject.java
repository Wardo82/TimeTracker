package core.DS.timetracker;

import java.util.Hashtable;
import java.util.Set;

public class CProject extends CActivity {

    public CProject(String pname, String pdescription) {
        super(pname, pdescription);
    }

/* Methods */

    @Override
    public void Accept(CVisitor visitor) {
        visitor.visitProject(this); // Visit this project
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over
        for (String key: keys) { // For each key in keys
            m_activities.get(key).Accept(visitor); // Let the visitor go to the child activities
        }
    }

    @Override
    public long getTotalTime() {
        /* getTotalTime: Gets total time of all contained activities. If the activity happens to be
        * Project it will call it "recursively" until all activities are Task which will then return
        * the time in milliseconds
        * @return total: Total number of combined milliseconds of all activities */
        long total = 0; // Total time to be returned
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over

        for (String key: keys) { // For each key in keys
            CActivity activity = m_activities.get(key); // Get the activity for the given key
            total = total + activity.getTotalTime(); // Update the total time
        }

        return total;
    }

    @Override
    public void appendObject(CActivity activity) {
        m_activities.put(activity.getName(), activity);
    }

    @Override
    public CActivity getObject(String id) {
        return m_activities.get(id);
    }

    public void trackTaskStart(String id) {
        CTask task = (CTask) m_activities.get(id); // Get task to track
        Thread taskThread = new Thread(task, id);
        taskThread.start();

        if (m_startTime == 0) { // If no other interval has been started the m_start time is 0
            m_startTime = CClock.getInstance().getTime(); // Ask clock for current time
        }
    }

    public void trackTaskStop(String id) {
        CTask task = (CTask) m_activities.get(id);
        task.trackTaskStop();
        m_endTime = CClock.getInstance().getTime();
    }

    @Override
    public long getCurrentTime() {
        // Iterate over all intervals and return the highest m_currentTime
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

/* Properties */
    private Hashtable<String, CActivity> m_activities = new Hashtable<>(); // Set of projects and activities the user has created.
}
