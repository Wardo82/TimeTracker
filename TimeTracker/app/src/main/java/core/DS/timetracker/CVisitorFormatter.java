/**
 * CVisitorFormatter: Visitor class that works as interface for the
 * different types of format for generating a report.
 * */
package core.ds.TimeTracker;

import java.text.SimpleDateFormat;
import java.util.Locale;

public abstract class CVisitorFormatter extends CVisitor {

    public CVisitorFormatter(final long start, final long end) { // Constructor with parameters
        m_startTime = start;
        m_currentTime = CClock.getInstance().getTime();
        m_endTime = end;
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public abstract void printHeader();
    public abstract void printLineSeparator();
    public abstract void printProjectsHeader();
    public abstract void printSubprojectsHeader();
    public abstract void printTasksHeader();
    public abstract void printIntervalsHeader();

    public abstract void generateReport();

    protected long m_startTime = 0;
    protected long m_currentTime = 0;
    protected long m_endTime = 0;

    protected SimpleDateFormat Day = new SimpleDateFormat("d/M/YY", Locale.US);
    protected SimpleDateFormat hour = new SimpleDateFormat("hh:mm", Locale.US);
    protected SimpleDateFormat duration = new SimpleDateFormat("H'h' m'm' s's'", Locale.US);
}
