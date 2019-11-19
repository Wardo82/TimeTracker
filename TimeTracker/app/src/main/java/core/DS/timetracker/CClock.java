package core.ds.TimeTracker;

import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.beans.PropertyChangeListener;

/**
 * Singleton class used to handle all time related tasks.
 * CClock extends Thread functionality to run in parallel to the main
 * function.
 * The implemented scheme works similar to a Publish-Subscribe pattern
 * where CClock announces the change of time to its listener.
 * The Singleton pattern was used so that there is only one clock running
 * per application.
 */
public final class CClock extends Thread {
    /**
     * Public method used to access the only Instance of CClock that should
     * run in the program.
     * @return Clock instance of the application */
    public static CClock getInstance() {
        if (Instance == null) {
            Instance = new CClock();
        }
        return Instance;
    }

    /**
     * Method used to make the thread alive while the main program is being
     * executed.
     */
    @Override
    public void run() { // Overriding from the Thread class
        while (m_running) { // Always run
            step();
        }
    }

    /**
     * Adds listener to the support of the of the property change functionality.
     * @param pcl The listener to be added */
    public void addPropertyChangeListener(final PropertyChangeListener pcl) {
        // Add a listener to the support object
        m_support.addPropertyChangeListener(pcl);
    }

    /**
     * Removes listener to the support of the of the property change functionality.
     * @param pcl The listener to be added */
    public void removePropertyChangeListener(final PropertyChangeListener pcl) {
        // Removes a listener from the support object
        m_support.removePropertyChangeListener(pcl);
    }

    /**
     * Step method called continuously (and in parallel to the main program)
     * to perform the pass of time. */
    public void step() {
        // Step function to be continuously called to perform the pass of time.
        try { // Try-Catch statement to deal with the interruption by another thread
            sleep(m_timeUnit);

            m_timeCounter = m_timeCounter + m_timeUnit; // Update the counter by 1 second
            m_support.firePropertyChange("Counter", 2, m_timeCounter); // Broadcast change in value of counter
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void turnOff() { // Set the run flag to false
        m_running = false;
    }
    public void turnOn() { // Set the run flag to true
        m_running = true;
    }

    public long getTimeUnit() {
        return m_timeUnit;
    }
    public long getTime() { // Returns the value of the current counter
        return m_timeCounter;
    }

    public void setTimeUnit(final long unit) {
        m_timeUnit = unit;
    }


    /** Private constructor for Singleton pattern. */
    private CClock() {
        m_running = true;
        // New PropertyChangeSupport to implement the publish-subscribe scheme
        m_support = new PropertyChangeSupport(this);
        // Initialize the counter to current real time
        m_timeCounter = Calendar.getInstance().getTimeInMillis();
    }

    private static CClock Instance = null;
    private long m_timeUnit; // The step size of the clock
    private long m_timeCounter; // The counter used by the system to measure time
    private PropertyChangeSupport m_support; // Object needed to perform the Publish-Subscribe tasks
    private boolean m_running; // Flag for killing the thread
}
