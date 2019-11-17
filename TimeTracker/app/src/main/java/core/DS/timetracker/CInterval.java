package core.ds.TimeTracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CInterval implements PropertyChangeListener, java.io.Serializable {

    public CInterval(String name, String description) {
        m_name = name;
        m_description = description;
    }

/* Methods */
    public void Accept(CVisitor visitor) {
        /*
         * Accept: Function used to implement the visitor pattern. It receives the visitor as parameter
         * to the send itself to the visitor for this class
         * @args visitor: Visitor variable that implements the desired functionality for this class */
        visitor.visitInterval(this); // Visit this interval
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("Counter") && m_running) {// For the case of many Observables, ask if it is the Counter
            m_currentTime = (long) evt.getNewValue(); // On event set m_currentTime to Clock's counter passed as NewValue
        }
    }

    /* Getters and setters */
    public String getName() { return m_name; }
    public String getDescription() { return m_description; }
    public String getProjectName() { return m_projectName; }
    public String getTaskParentName() { return m_taskName; }
    public long getStartTime() { return m_startTime; }
    public long getEndTime() { return m_endTime; }
    public long getCurrentTime() { return m_currentTime; }

    public void setProjectName(String name) { m_projectName = name; }
    public void setTaskParentName(String name) { m_taskName = name; }
    public void setEndTime(long endTime) { m_endTime = endTime; }
    public void setStartTime(long startTime) { m_startTime = startTime; }

    public void start() {
        m_running = true;
        m_startTime = CClock.getInstance().getTime();
        m_currentTime = m_startTime;
        CClock.getInstance().addPropertyChangeListener(this); // Add interval as listener to the Clock
    }

    public void end() {
        m_running = false;
        m_endTime = m_currentTime;
        CClock.getInstance().removePropertyChangeListener(this);

    }

    public long getTotalTime() {
        /* Get the time the Interval has been alive. If it has not started m_currentTime and m_startTime
        are the same so it returns 0. If it is finished, m_currentTime is the last time recorded by the
        interval
         */
        return (m_currentTime-m_startTime);
    }

    public long getTotalTimeWithin(long start, long end) {
        long total = 0;

        if(start >= m_currentTime || end <= m_startTime){ // If it is out of bounds
            total = total; // Do nothing
        }else if(m_startTime < start && m_currentTime <= end){ // If the interval started before and ended inside
            total = m_currentTime - start; // The right part of the interval inside the bound
        }else if(start <= m_startTime && m_currentTime <= end){ // If it is completely inside the bound
            total = m_currentTime - m_startTime; // The whole interval duration
        }else if(m_startTime < end && m_startTime > start &&
                m_currentTime > end) { // If the interval started in the bound and ended outside
            total = end - m_startTime;
        }else if(m_startTime < end && m_currentTime > end) { // If the interval was running during the bound
            total = end-start;
        }

        return total;
    }

    /* Properties */
    private boolean m_running = false;
    private String m_name;
    private String m_description;
    private String m_taskName;
    private String m_projectName;
    private long m_startTime = 0;
    private long m_currentTime = 0;
    private long m_endTime = 0;
}
