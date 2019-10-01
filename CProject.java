package core.DS.timetracker;

import java.util.Hashtable;
import java.util.Set;

public class CProject extends CActivity {

/* Methods */
    public long getTotalTime() {
        /* getTotalTime: */
        long total = 0; // Total time to be returned
        Set<String> keys = m_activities.keySet(); // Set of keys to iterate over

        for (String key: keys) { // For each key in keys
            CActivity activity = m_activities.get(key); // Get the activity for the given key

            if (activity instanceof CProject) { // If the activity is a Project
                CProject p = (CProject) activity; // Cast it to a project
                total += p.getTotalTime(); // Get total in a recursive manner

            }else if(activity instanceof CTask) { // If the activity is a Task
                CTask t = (CTask) activity; // Cast it to a project
                total += t.getTotalTime(); // Get total of this task

            }
        }

        return total;
    }

/* Properties */
    private Hashtable<String, CActivity> m_activities = new Hashtable<>(); // Set of projects and activities the user has created.
    private CActivity act;
}
