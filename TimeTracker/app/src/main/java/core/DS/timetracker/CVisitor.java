/**
 * Main Visitor class used as interface for all implementations of visitors.
 * CActivity and CInterval have a method Accept() used to send this object
 * as parameter and call the corresponding function for a given receiver
 * (see CProject and CTask) .
 */
package core.ds.TimeTracker;

public abstract class CVisitor {

    public CVisitor() {
        m_forward = true; // Forwarded by default
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public boolean isForwarded() { return m_forward; }
    public void setParentName(final String name) { m_parentName = name; }

    protected String m_parentName = null;
    protected boolean m_forward;
}
