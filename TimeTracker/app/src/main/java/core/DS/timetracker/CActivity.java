package core.ds.TimeTracker;

public abstract class CActivity implements java.io.Serializable {
    /**
    * CActivity: Abstract class for which CProjects and CTasks will be used.
     * The Time Tracker Engine works on a list of activities, appending and removing projects and task
     * as desired.
     * CActivity and CProject follow the Composite pattern which makes CProject able of storing CActivities,+
     * that is, CProject contains a vector of other CProjects and CTasks.
    * **/
    public CActivity() {} // Constructor
    public CActivity(String name, String description) { // Constructor with parameters
        m_name = name;
        m_description = description;
        m_projectParentName = null;
    }

/* Methods */
    // Accept abstract function used to implement the Visitor pattern
    public abstract void Accept(CVisitor visitor);
    public abstract long getTotalTime(); // Get total time since begining of activity creation
    public abstract long getTotalTimeWithin(long start, long end); // Gets total time within bounds
    // Getters
    public String getName() { return m_name; }
    public String getDescription() { return m_description; }
    public String getProjectParentName() { return m_projectParentName; }

    public long getStartTime() { return m_startTime; }
    public long getEndTime() { return m_endTime; }
    public long getCurrentTime() { return m_currentTime; }

    // Setters

    public void setProjectParentName(String name) { m_projectParentName = name; }
    public void setEndTime(long endTime) { m_endTime = endTime; }
    public void setStartTime(long startTime) { m_startTime = startTime; }

/* Properties */
    private String m_name;
    private String m_description;
    protected String m_projectParentName;
    protected long m_startTime = 0;
    protected long m_currentTime = 0;
    protected long m_endTime = 0;

}
