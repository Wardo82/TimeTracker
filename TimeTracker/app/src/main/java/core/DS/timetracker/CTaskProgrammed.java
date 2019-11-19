package core.ds.TimeTracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Class extending <code>CTask</code> functionality.
 * This object should start at a given time (see constructor).
 * It implements <code>PropertyChangeListener</code> interface to know
 * when to start itself after the start time has been reached.
 * The constructor also adds the class to the listeners of the Clock
 * guaranteeing that time is the application's time.
 * **/
public class CTaskProgrammed extends CTaskDecorator
        implements PropertyChangeListener {

    /** Decorator constructor with start time as parameter.
     * @param task The task wrapped by the decorator
     * @param start The start time for this task to be active */
    public CTaskProgrammed(final CTask task, final long start) {
        super(task);
        m_startTime = start;
        CClock.getInstance().addPropertyChangeListener(this);
    }

    /**
     * Implements <code>PropertyChangeListener</code> interface to call
     * <i>trackTaskStart</i> when the time for start is reached. */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("Counter")) { //ask if it is the Counter
            // On event set m_currentTime to Clock's counter passed as NewValue
            this.m_task.m_currentTime = (long) evt.getNewValue();
            // If the current time is greater than the planned start time
            if (this.m_task.m_currentTime > this.m_startTime) {
                this.trackTaskStart(); // Start tracking this task
                // Stop listening the Clock, the Intervals will handle from here
                CClock.getInstance().removePropertyChangeListener(this);
            }
        }
    }

}
