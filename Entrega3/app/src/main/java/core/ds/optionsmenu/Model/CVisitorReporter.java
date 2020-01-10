package core.ds.optionsmenu.Model;

/**
 * Visitor abstract class that works as interface for the different levels
 * of description required by a client to generating a report.
 * The <code>CVisitorReporter</code> wraps a <code>CVisitorFormatter</code>
 * in order to print in the desired format.
 */
public abstract class CVisitorReporter extends CVisitor {

    /** Initialize the reporter with the specified formatter.
     * @param formatter The pre-instantiated formatter object */
    public CVisitorReporter(final CVisitorFormatter formatter) {
        m_formatter = formatter;
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public abstract void generateReport();

    public CVisitorFormatter getFormatter() {
        return m_formatter;
    }

    public void setFormatter(final CVisitorFormatter formatter) {
        m_formatter = formatter;
    }

    protected CVisitorFormatter m_formatter;
}
