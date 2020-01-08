package core.ds.TimeTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

/**
 * Visitor Subclass of <code>CVisitorReporter</code> that implements
 * the brief type of report
 * The layout for this Report is
 * --- Main title ---
 * Report header
 * ------------------
 * - Project title -
 * Projects
 * ------------------
 */
public class CVisitorReporterBrief extends CVisitorReporter {

    public CVisitorReporterBrief(final CVisitorFormatter formatter) { // Constructor with parameters
        super(formatter); // Calls superclass constructor
        m_forward = false;
        m_formatter.m_forward = false;
    }

    public void visitProject(final CProject project) {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting {}", project.getName());
        }
        m_projects.add(project); // Append project to projects list
    };

    public void visitTask(final CTask task) { };

    public void visitInterval(final CInterval interval) { };

    public void generateReport() {
        m_formatter.appendHeader();
        m_formatter.appendProjectsHeader();
        for (CProject p: m_projects) {
            p.Accept(m_formatter);
        }
        m_formatter.appendProjectsData();
        m_formatter.appendLineSeparator();
        m_formatter.generateReport();
    }

    protected Vector<CProject> m_projects = new Vector();
    private static Logger logger = LoggerFactory.getLogger(CVisitorReporterBrief.class);
}
