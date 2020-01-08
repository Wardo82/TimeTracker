package core.ds.optionsmenu.Model;

import android.os.Handler;
import android.util.Log;

/**
 * Used as substitution of the threading mechanism used by CClock.
 * Periodically invokes <code>update</code> of an object that subscribes
 * to this class.
 */
public class CUpdater extends Handler {

    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Update period in milliseconds.
     */
    private long m_delayMilli;
    /**
     * Flag to invoke the update mechanisms.
     */
    private boolean m_running;
    /**
     * See <em>Updating the UI from a Timer</em> for more.
     */
    private Handler m_Handler = new Handler();
    /**
     * Object of which we will call <code>update</code>
     * . Its class should implement the interface {@link IUpdatable}.
     */
    private IUpdatable m_updatable;

    /**
     * Useful to identify the updated object in the logging messages.
     */
    private String m_owner;

    /**
     * Class constructor but doesn't start it.
     *
     * @param up
     *            objecte a actualitzar periòdicament
     * @param d
     *            període en milisegons
     * @param str
     *            identificador de l'objecte actualitzat pels missatges de
     *            logging
     */
    public CUpdater(final IUpdatable up, final long d,
                         final String str) {
        super();
        m_running = false;
        m_delayMilli = d;
        m_updatable = up;
        m_owner = str;
    }

    /**
     * Starts the update cycles after {@link #m_delayMilli}
     * milliseconds with this period.
     */
    public final void start() {
        if (!m_running) {
            m_running = true;
            m_Handler.postDelayed(m_UpdateTimeTask, m_delayMilli);
            Log.d(TAG,   m_owner + "'s handler started.");
        } else {
            Log.d(TAG,   m_owner + "'s handler was already started.");
        }
    }

    /**
     * See <em>Updating the UI from a Timer</em> for more.
     */
    private Runnable m_UpdateTimeTask = new Runnable() {
        public void run() {
            Log.d(TAG, "run() updater of " + m_owner);
            if (m_running) {
                Log.d(TAG, " and update");
                m_Handler.removeCallbacks(m_UpdateTimeTask);
                m_updatable.update();
                m_Handler.postDelayed(m_UpdateTimeTask, m_delayMilli);
            } else {
                Log.d(TAG, "Don't do anything");
            }
        }
    };

    /**
     * Momentarily stops the cycles of updates started in {@link #start}.
     */
    public final void stop() {
        if (m_running) { // si ja estava engegat
            m_running = false;
            m_Handler.removeCallbacks(m_UpdateTimeTask);
            Log.d(TAG, "handler stopped.");
        } else {
            Log.d(TAG, "Handler already stopped.");
        }
    }

};
