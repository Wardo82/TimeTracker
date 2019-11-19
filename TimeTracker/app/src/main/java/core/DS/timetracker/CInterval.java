package core.ds.TimeTracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Class used for handling the time a task has been active.
 * CTask is the only class that can contain an interval and is
 * responsible for creating and destroying them (see CTask).
 */
public class CInterval implements PropertyChangeListener, java.io.Serializable {

    /** Initialize name and description of this interval.
     * @param name The name of the interval.
     * @param description The description of the interval. */
    public CInterval(final String name, final String description) {
        m_name = name;
        m_description = description;
    }

    /**
     * Function used to implement the visitor pattern. It receives
     * the visitor as parameter to the send itself to the visitor for this class
     * @param  visitor Visitor that implements the desired functionality for
     *                 this class */
    public void Accept(final CVisitor visitor) {
        visitor.visitInterval(this);
    }

    /**
     * Implements <code>PropertyChangeListener</code> interface to update the
     * current time. */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("Counter") && m_running) { // For the case of many Observables, ask if it is the Counter
            m_currentTime = (long) evt.getNewValue(); // On event set m_currentTime to Clock's counter passed as NewValue
        }
    }

    public String getName() { return m_name; }
    public String getDescription() { return m_description; }
    public String getProjectName() { return m_projectName; }
    public String getTaskParentName() { return m_taskName; }
    public long getStartTime() { return m_startTime; }
    public long getEndTime() { return m_endTime; }
    public long getCurrentTime() { return m_currentTime; }

    public void setProjectName(final String name) { m_projectName = name; }
    public void setTaskParentName(final String name) { m_taskName = name; }
    public void setEndTime(final long endTime) { m_endTime = endTime; }
    public void setStartTime(final long startTime) { m_startTime = startTime; }

    /**
     * Starts tracking the interval and adds it to the listeners of the Clock.
     * To be called from CTask class. */
    public void start() {
        m_running = true;
        m_startTime = CClock.getInstance().getTime();
        m_currentTime = m_startTime;
        CClock.getInstance().addPropertyChangeListener(this); // Add interval as listener to the Clock
    }

    /**
     * Ends recording the interval and removes it from the listeners to the Clock.
     * To be called from CTask class. */
    public void end() {
        m_running = false;
        m_endTime = m_currentTime;
        CClock.getInstance().removePropertyChangeListener(this);
    }

    /**
     * Get the time the Interval has been alive.
     * @return Total time for this interval*/
    public long getTotalTime() {
        return (m_currentTime - m_startTime);
    }

    /**
     * Get the time the Interval has been alive within the bounds of
     * the parameters.
     * @param start the left point in time of the bound
     * @param end the right point in time of the bound.
     * @return Total time of this interval inside the parameter bound.*/
    public long getTotalTimeWithin(final long start, final long end) {
        long total = 0;

        if (start >= m_currentTime || end <= m_startTime) { // If it is out of bounds
            total = total; // Do nothing
        } else if (m_startTime < start && m_currentTime <= end) { // If the interval started before and ended inside
            total = m_currentTime - start; // The right part of the interval inside the bound
        } else if (start <= m_startTime && m_currentTime <= end) { // If it is completely inside the bound
            total = m_currentTime - m_startTime; // The whole interval duration
        } else if (m_startTime < end && m_startTime > start
                    && m_currentTime > end) { // If the interval started in the bound and ended outside
            total = end - m_startTime;
        } else if (m_startTime < end && m_currentTime > end) { // If the interval was running during the bound
            total = end - start;
        }

        return total;
    }

    private boolean m_running = false;
    private String m_name;
    private String m_description;
    private String m_taskName;
    private String m_projectName;
    private long m_startTime = 0;
    private long m_currentTime = 0;
    private long m_endTime = 0;
}
