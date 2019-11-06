package core.DS.timetracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CTaskLimitedTime extends CTaskDecorator implements PropertyChangeListener {
    /**
     * CTaskLimitedTime: Class extending BasicTask functionality. This object should end after a given
     * amount of time.
     * **/
    public CTaskLimitedTime(CTask task, long maxTime) { // Decorator constructor with maximum time as parameter
        super(task); // Calls CTaskDecorator constructor
        m_maxTime = maxTime; // Maximum time set by the user for this task
        CClock.getInstance().addPropertyChangeListener(this); // Make this object listen to Clock events
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("Counter")) {// For the case of many Observables, ask if it is the Counter
            this.m_task.m_currentTime = (long) evt.getNewValue(); // On event set m_currentTime to Clock's counter passed as NewValue
            if (this.m_task.getCurrentTime() - this.m_task.m_startTime > m_maxTime) { // When the time is over
                this.trackTaskStop(); // Stop tracking this task
            }
        }
    }

    @Override
    public void trackTaskStart() {
        super.trackTaskStart();
        this.m_task.setStartTime( CClock.getInstance().getTime() );
    }

    @Override
    public void trackTaskStop() {
        super.trackTaskStop();
        CClock.getInstance().removePropertyChangeListener(this);
    }

    /* Properties */
    private long m_maxTime;
}
