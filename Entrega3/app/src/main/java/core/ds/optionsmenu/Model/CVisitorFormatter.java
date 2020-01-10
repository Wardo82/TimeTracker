package core.ds.optionsmenu.Model;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Visitor abstract class that works as interface for the different types
 * of format for generating a report.
 * <code>append</code><i>ObjectName</i> methods are declared to specify in
 * the <code>generateReport()</code> method the desired layout of the report.
 */
public abstract class CVisitorFormatter extends CVisitor {

    /** Initialize start and end of the period for this report.
     * @param start Start of the report period (left bound).
     * @param end End of the report period (right bound). */
    public CVisitorFormatter(final long start, final long end) {
        m_startTime = start;
        m_currentTime = CClock.getInstance().getTime();
        m_endTime = end;
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public abstract void appendLineSeparator();
    public abstract void appendHeader();
    public abstract void appendProjectsHeader();
    public abstract void appendProjectsData();
    public abstract void appendSubProjectsHeader();
    public abstract void appendSubProjectsData();
    public abstract void appendTasksHeader();
    public abstract void appendTasksData();
    public abstract void appendIntervalsHeader();
    public abstract void appendIntervalsData();

    public abstract void generateReport();

    protected long m_startTime = 0;
    protected long m_currentTime = 0;
    protected long m_endTime = 0;

    protected SimpleDateFormat Day = new SimpleDateFormat("d/M/YY", Locale.US);
    protected SimpleDateFormat hour = new SimpleDateFormat("hh:mm", Locale.US);
    protected SimpleDateFormat duration = new SimpleDateFormat("H'h' m'm' s's'", Locale.US);
}
