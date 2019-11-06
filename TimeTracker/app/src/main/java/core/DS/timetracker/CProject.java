package core.DS.timetracker;

import java.util.Collections;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

public class CProject extends CActivity {
    /**
     * CProject: Class representing the projects of the application. Projects can contain other projects
     * and tasks (See CTask). This is done by making use of the Composite pattern (See CActivity).
    * **/
    public CProject(String pname, String pdescription) { // Constructor with parameters
        super(pname, pdescription); // Calls superclass constructor
    }

/* Methods */
    @Override
    public void Accept(CVisitor visitor) {
        /*
        * Accept: Function used to implement the visitor pattern. It receives the visitor as parameter
        * to the send itself to the visitor for this class
        * @args visitor: Visitor variable that implements the desired functionality for this class */
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

    public void appendActivity(CActivity activity) { m_activities.put(activity.getName(), activity); } // Add an activity to this project
    public CActivity getActivity(String id) {
        return m_activities.get(id);
    } // Get activity with id

    public void trackTaskStart(String id) {
        /* trackTaskStart: Gets the task with id and begins the tracking of the activity
        * @args id: Id of the desired task */
        try {
            CTask task = (CTask) m_activities.get(id); // Get task to track
            task.trackTaskStart(); // Start tracking task with id
        }catch (ClassCastException e){
            System.out.println("The given id does not belong to a Task");
            e.printStackTrace();
        }
    }

    public void trackTaskStop(String id) {
        /* trackTaskStop: Gets the task with id and stops the tracking of the activity
         * @args id: Id of the desired task */
        try {
            CTask task = (CTask) m_activities.get(id);  // Get task to stop
            task.trackTaskStop(); // Stop tracking task with id
        }catch (ClassCastException e) {
            System.out.println("The given id does not belong to a Task");
            e.printStackTrace();
        }
        m_endTime = CClock.getInstance().getTime(); // Set end time to the current time of the Clock
    }

    // TODO: Arreglar estas dos chapuzas con un visitor VisitorUpdate o algo parecido
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

    @Override
    public long getStartTime() {
        // Iterate over all intervals and return the lowest m_startTime
        Set<String> keys = m_activities.keySet(); // Set of keys of the hashtable
        Vector<Long> times = new Vector<>();
        for (String key: keys) { // For each key in the set
            long time = m_activities.get(key).getStartTime(); // Get the element from m_intervals and get start time
            if (time > 0) { // If is not 0 add it to vector times
                times.add(time);
            }
        }

        try{
            m_startTime = Collections.min(times); // Get the lowest recorded starting time
        }catch (NoSuchElementException e) {
            m_startTime = 0;
        }
        return m_startTime;
    }

/* Properties */
    private Hashtable<String, CActivity> m_activities = new Hashtable<>(); // Set of projects and activities the user has created.
}
