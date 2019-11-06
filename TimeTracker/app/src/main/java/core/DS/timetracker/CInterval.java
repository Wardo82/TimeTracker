package core.DS.timetracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CInterval implements PropertyChangeListener, java.io.Serializable {

    public CInterval(String name, String description) {
        m_name = name;
        m_description = description;
    }

/* Methods */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("Counter") && m_running) {// For the case of many Observables, ask if it is the Counter
            m_currentTime = (long) evt.getNewValue(); // On event set m_currentTime to Clock's counter passed as NewValue
        }
    }

    /* Getters and setters */
    public String getName() { return m_name; }
    public String getDescription() { return m_description; }
    public long getStartTime() { return m_startTime; }
    public long getEndTime() { return m_endTime; }
    public long getCurrentTime() { return m_currentTime; }

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

    /* Properties */
    private boolean m_running = false;
    private String m_name;
    private String m_description;
    private long m_startTime = 0;
    private long m_currentTime = 0;
    private long m_endTime = 0;
}
