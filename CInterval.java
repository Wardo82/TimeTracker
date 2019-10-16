package core.DS.timetracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CInterval extends CActivity implements PropertyChangeListener {

    public CInterval(String name, String description) {
        super(name, description);
    }

/* Methods */
    @Override
    public void Accept(CVisitor visitor) { visitor.visitInterval(this); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("Counter") && m_running) {// For the case of many Observables, ask if it is the Counter
            m_currentTime = (long) evt.getNewValue(); // On event set m_currentTime to Clock's counter passed as NewValue
        }
    }

    /* Getters and setters */
    public void start() {
        m_running = true;
        m_startTime = CClock.getInstance().getTime();
        m_currentTime = m_startTime;
    }

    public void end() {
        m_running = false;
        m_endTime = m_currentTime;
    }

    @Override
    public long getTotalTime() {
        /* Get the time the Interval has been alive. If it has not started m_currentTime and m_startTime
        are the same so it returns 0. If it is finished, m_currentTime is the last time recorded by the
        interval
         */
        return (m_currentTime-m_startTime);
    }

    @Override
    public void appendObject(CActivity activity) {}

    @Override
    public CActivity getObject(String id) { return null; }

    /* Properties */
    private boolean m_running = false;
}
