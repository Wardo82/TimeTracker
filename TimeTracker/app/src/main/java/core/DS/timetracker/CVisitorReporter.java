package core.ds.TimeTracker;

import java.util.Vector;

public abstract class CVisitorReporter extends CVisitor {

    public CVisitorReporter(CVisitorFormatter formatter) {
        m_formatter = formatter;
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public abstract void generateReport();

    public CVisitorFormatter getFormatter() {
        return m_formatter;
    }

    public void setFormatter(CVisitorFormatter m_formatter) {
        this.m_formatter = m_formatter;
    }

    protected CVisitorFormatter m_formatter;
}
