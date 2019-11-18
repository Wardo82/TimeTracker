/**
 * CActivity: Abstract class for which CProjects and CTasks will be used.
 * The Time Tracker Engine works on a list of activities, appending and
 * removing projects and task as desired.
 * CActivity and CProject follow the Composite pattern which makes CProject
 * able of storing CActivities, that is, CProject contains a vector of other
 * CProjects and CTasks.
 */
package core.ds.TimeTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CActivity implements java.io.Serializable {

    public CActivity() { }
    public CActivity(final String name, final String description) { // Constructor with parameters
        m_name = name;
        m_description = description;
        m_projectParentName = null;
    }

    /**
     * Accept: Function used to implement the visitor pattern. It receives
     * the visitor as parameter to the send itself to the visitor for this class
     * @param  visitor Visitor that implements the desired functionality for
     *                 this class */
    public abstract void Accept(CVisitor visitor); // Accept method with capital A to follow lecture guidelines

    // Getters
    public abstract long getTotalTime(); // Get total time since beginning of activity creation

    /** getTotalTimeWithin: Gets total time of all contained activities within
     * bounds (start, end).
     * @param start Start of the period of interest
     * @param end End of the period of interest
     * @return total: Total number of combined milliseconds of all activities */
    public abstract long getTotalTimeWithin(long start, long end);

    public String getName() { return m_name; };
    public String getDescription() { return m_description; }
    public String getProjectParentName() { return m_projectParentName; }

    public long getStartTime() { return m_startTime; }
    public long getEndTime() { return m_endTime; }
    public long getCurrentTime() { return m_currentTime; }

    // Setters
    public void setProjectParentName(final String name) { m_projectParentName = name; }
    public void setEndTime(final long endTime) { m_endTime = endTime; }
    public void setStartTime(final long startTime) { m_startTime = startTime; }

    // Properties
    private String m_name;
    private String m_description;
    protected String m_projectParentName;
    protected long m_startTime = 0;
    protected long m_currentTime = 0;
    protected long m_endTime = 0;

    static Logger logger = LoggerFactory.getLogger("TimeTracker.CActivity");

}
