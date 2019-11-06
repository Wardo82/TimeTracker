package core.DS.timetracker;

import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.beans.PropertyChangeListener;

public class CClock extends Thread {
    /**
     * CClock: Singleton class used to handle all time related tasks. CClock extends Thread functionality
     * to run in parallel to the main function.
     * The implemented scheme works similar to a Publish-Subscribe pattern where CClock announces the change
     * of time to its listener.
     * The Singleton pattern was used so that there is only one clock running per application.
     * **/

/* Methods */
    public static CClock getInstance() {
        /* getInstace: Method used to get the CClock object. */
        if (Instance == null) { // If it has not been instantiated
            Instance = new CClock(); // Initialize a new one
        }
        return Instance;
    }

    @Override
    public void run() { // Overriding from the Thread class
        while (m_running) { // Always run
            step();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        // Add a listener to the support object
        m_support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        // Removes a listener from the support object
        m_support.removePropertyChangeListener(pcl);
    }

    public void step() {
        // Step function to be continuously called to perform the pass of time.
        try { // Try-Catch statement to deal with the interruption by another thread
            sleep(m_timeUnit);

            m_timeCounter = m_timeCounter+m_timeUnit; // Update the counter by 1 second
            m_support.firePropertyChange("Counter", 2, m_timeCounter); // Broadcast change in value of counter
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void turnOff(){ // Set the run flag to false
        m_running = false;
    }
    public void turnOn() { m_running = true; } // Set the run flag to true
    public long getTimeUnit() { return m_timeUnit; }
    public long getTime() { // Returns the value of the current counter
        return m_timeCounter;
    }

    public void setTimeUnit(long unit) { m_timeUnit = unit; }

/* Properties */
    private CClock() { // Private constructor for Singleton pattern
        m_running = true;
        m_support = new PropertyChangeSupport(this); // New PropertyChangeSupport to implement the publish-subscribe scheme
        m_timeCounter = Calendar.getInstance().getTimeInMillis(); // Initialize the counter to current real time
    }

    private static CClock Instance = null; // The instance used for the Singleton pattern
    private long m_timeUnit = 2000; // 2000 ms are 2 seconds
    private long m_timeCounter; // The counter used by the system to measure time
    private PropertyChangeSupport m_support; // Object needed to perform the Publish-Subscribe tasks
    private boolean m_running; // Flag for killing the thread
}
