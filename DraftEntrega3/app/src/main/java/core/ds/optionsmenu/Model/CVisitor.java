package core.ds.optionsmenu.Model;

/**
 * Main Visitor class used as interface for all implementations of visitors.
 * CActivity and CInterval have a method Accept() used to send this object
 * as parameter and call the corresponding function for a given receiver
 * (see CProject and CTask).
 */
public abstract class CVisitor {

    public CVisitor() {
        m_forward = true; // Forwarded by default
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public boolean isForwarded() {
        return m_forward;
    }

    public void setParentName(final String name) { m_parentName = name; }

    /** Name of the parent Activity that sent the visitor. */
    protected String m_parentName = null;
    /** Flag to check if visitor is to be sent to children. */
    protected boolean m_forward;
}
