package core.ds.optionsmenu.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Class extending <code>CTask</code> functionality.
 * This object should end after a given amount of time (see constructor).
 * It implements <code>PropertyChangeListener</code> interface to know
 * when to stop itself after the set limit time has been passed.
 * The constructor also adds the class to the listeners of the Clock
 * guaranteeing that time is the application's time.
 * **/
public class CTaskLimitedTime extends CTaskDecorator
        implements PropertyChangeListener {

    /** Decorator constructor with maximum time as parameter.
     * @param task The task wrapped by the decorator
     * @param maxTime The maximum amount of time this task will remain active */
    public CTaskLimitedTime(final CTask task, final long maxTime) {
        super(task); // Calls CTaskDecorator constructor
        m_maxTime = maxTime; // Maximum time set by the user for this task
        // Make this object listen to Clock events
        CClock.getInstance().addPropertyChangeListener(this);
    }

    /**
     * Implements <code>PropertyChangeListener</code> interface to call
     * <i>trackTaskStop</i> when the active time is over. */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // For the case of many Observables, ask if it is the Counter
        if (evt.getPropertyName().equals("Counter")) {
            // On event set m_currentTime to Clock's counter passed as NewValue
            this.m_task.m_currentTime = (long) evt.getNewValue();
            if (this.m_task.getCurrentTime() - this.m_task.m_startTime
                    > m_maxTime) { // When the time is over
                this.trackTaskStop(); // Stop tracking this task
            }
        }
    }

    /**
     * Starts tracking the time and sets the start time to the current Clock
     * time. */
    @Override
    public void trackTaskStart() {
        super.trackTaskStart();
        this.m_task.setStartTime(CClock.getInstance().getTime());
    }

    /**
     * Stops tracking the time and removes this object from the Clock's
     * <code>PropertyChangeListener</code>s. */
    @Override
    public void trackTaskStop() {
        super.trackTaskStop();
        CClock.getInstance().removePropertyChangeListener(this);
    }

    private long m_maxTime;
}
