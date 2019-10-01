package core.DS.timetracker;

import java.util.Date;

public class CInterval {

/* Methods */
    /* Getters and setters */
    public Date getEnd() { return m_end; }
    public Date getStart() { return m_start; }

    public long getTime() {
        return (m_end.getTime() - m_start.getTime());
    }

    public Date requestTime() {
        return CClock.getInstance().getTime(); // TODO: The request should be in a separate threat
    }

    /* Properties */
    private Date m_start = new Date();
    private Date m_end = new Date();
}
