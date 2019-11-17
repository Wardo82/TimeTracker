package core.ds.TimeTracker;

public abstract class CVisitorFormatter extends CVisitor {

    public CVisitorFormatter(long start, long end) { // Constructor with parameters
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

    protected long m_startTime = 0;
    protected long m_currentTime = 0;
    protected long m_endTime = 0;
}
