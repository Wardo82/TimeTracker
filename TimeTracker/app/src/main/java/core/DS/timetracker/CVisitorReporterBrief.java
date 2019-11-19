package core.ds.TimeTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public class CVisitorReporterBrief extends CVisitorReporter {

    public CVisitorReporterBrief(final CVisitorFormatter formatter) { // Constructor with parameters
        super(formatter); // Calls superclass constructor
        m_forward = false;
        m_formatter.m_forward = false;
    }

    public void visitProject(CProject project) {
        logger.debug("Visiting {}", project);
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

        m_formatter.appendLineSeparator();
        m_formatter.generateReport();
    }

    protected Vector<CProject> m_projects = new Vector();
    private static Logger logger = LoggerFactory.getLogger(CVisitorReporterDetailed.class);
}
