package core.DS.timetracker;

import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

public class CTask extends CActivity {

/* Methods */
    /* Getters and setters */

    public Hashtable<Short, CInterval> getIntervals() { return m_intervals; }
    public Date getStartTime() { return m_startTime; }
    public Date getEndTime() { return m_endTime; }


    public void setEndTime(Date m_endTime) {
        this.m_endTime = m_endTime;
    }

    public long getTotalTime() {
        /* getTotalTime */
        long total = 0;
        Set<Short> keys = m_intervals.keySet();
        for (Short key: keys) {
            total = total + m_intervals.get(key).getTime();
        }
        return total;
    }

/* Properties */
    private Hashtable<Short, CInterval> m_intervals = new Hashtable<>();
    private Date m_startTime = new Date();
    private Date m_endTime = new Date();
    private CInterval inte;
}
