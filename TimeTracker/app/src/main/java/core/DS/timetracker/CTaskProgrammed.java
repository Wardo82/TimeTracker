package core.ds.TimeTracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CTaskProgrammed extends CTaskDecorator implements PropertyChangeListener {

    public CTaskProgrammed(CTask task, long start) {
        super(task);
        m_startTime = start;
        CClock.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("Counter")) {// For the case of many Observables, ask if it is the Counter
            this.m_task.m_currentTime = (long) evt.getNewValue(); // On event set m_currentTime to Clock's counter passed as NewValue
            if (this.m_task.m_currentTime > this.m_startTime) { // If the current time is greater than the planned start time
                this.trackTaskStart(); // Start tracking this task
                CClock.getInstance().removePropertyChangeListener(this); // Stop listening the Clock, the Intervals will handle from here
            }
        }
    }

}
