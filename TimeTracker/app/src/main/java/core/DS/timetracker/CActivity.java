package core.ds.TimeTracker;

/**
 * Abstract class for which <code>CProject</code>s and <code>CTask</code>s
 * will be used.
 * The <code>TimeTrackerEngine</code> works on a list of activities, appending
 * and removing projects and task as desired.
 * <code>CProject</code> and <code>CActivity</code> follow the Composite
 * pattern which makes <code>CProject</code> able of storing
 * <code>CActivity</code>s, that is, CProject contains a vector of other
 * <code>CProject</code>s and <code>CTask</code>s.
 */
public abstract class CActivity implements java.io.Serializable {

    /** Implementation left for subclasses.
     * */
    public CActivity() { }

    /** Initialize name and description of this activity.
     * @param name The name of the activity.
     * @param description The description of the activity. */
    public CActivity(final String name, final String description) {
        m_name = name;
        m_description = description;
        m_projectParentName = null;
    }

    /**
     * Function used to implement the visitor pattern. It receives
     * the visitor as parameter to the send itself to the visitor for this class
     * @param  visitor Visitor that implements the desired functionality for
     *                 this class */
    public abstract void Accept(CVisitor visitor); // Accept method with capital A to follow lecture guidelines

    /** Get total time since beginning of activity creation.
     * @return Total time. */
    public abstract long getTotalTime();

    /** Gets total time of all contained activities within
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
}
