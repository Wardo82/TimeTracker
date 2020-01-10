package core.ds.optionsmenu.Model;

import java.util.Date;
import java.util.Observable;

public class CClockUpdatable extends Observable implements IUpdatable {

    private CUpdater m_clockUpdater;
    private final int m_delayMilli = 1000;

    private static CClockUpdatable theInstance = null;

    private CClockUpdatable() {
        setHora(new Date());
        m_clockUpdater = new CUpdater(this,m_delayMilli,"Clock");
    }
    /**
     * Singleton
     * @return CClockUpdatable
     */
    public static CClockUpdatable Instance() {
        if (theInstance==null) {
            theInstance = new CClockUpdatable();
        }
        return theInstance;
    }

    private Date m_time;

    public Date getTime() {
        return m_time;
    }

    public void setHora(final Date hora) {
        m_time = hora;
    }

    private void tick() {
        setHora(new Date());
        setChanged();
        notifyObservers(this);
    }

    @Override
    public void update() {
        tick();
    }

    public void stop() {
        m_clockUpdater.stop();
    }

    public void start() {
        m_clockUpdater.start();
    }

}
