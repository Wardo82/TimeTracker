package core.DS.timetracker;

import java.util.Date;

public abstract class CActivity {
    /*
    * CActivity: Abstract class for which CProjects and CTasks will be used. The Time Tracker Engine
    * will work on a list of activities.
    * */
    public CActivity(String name, String description) {
        m_name = name;
        m_description = description;
    }

/* Methods */
    public abstract void Accept(CVisitor visitor);
    public abstract long getTotalTime(); // Abstract method follows Liskov substitution principle
    public abstract void appendObject(CActivity obj);
    public abstract CActivity getObject(String id);

    public String getName() { return m_name; }
    public String getDescription() { return m_description; }
    public long getStartTime() { return m_startTime; }
    public long getEndTime() { return m_endTime; }
    public long getCurrentTime() { return m_currentTime; }

    public void setEndTime(long endTime) { m_endTime = endTime; }
    public void setStartTime(long startTime) { m_startTime = startTime; }

/* Properties */
    private String m_name;
    private String m_description;
    protected long m_startTime = 0;
    protected long m_currentTime = 0;
    protected long m_endTime = 0;

}
