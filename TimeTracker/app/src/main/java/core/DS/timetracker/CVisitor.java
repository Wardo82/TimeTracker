package core.ds.TimeTracker;

public abstract class CVisitor {

    public CVisitor() { // Constructor
        m_forward = true; // Forwarded by default
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public boolean isForwarded() { return m_forward; }
    public void setParentName(String name) { m_parentName = name; }

    protected String m_parentName = null;
    protected boolean m_forward;
}
