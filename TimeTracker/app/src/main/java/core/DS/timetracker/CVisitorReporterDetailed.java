package core.ds.TimeTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public class CVisitorReporterDetailed extends CVisitorReporter {

    public CVisitorReporterDetailed(final CVisitorFormatter formatter) { // Constructor with parameters
        super(formatter); // Calls superclass constructor
        m_forward = true;
        m_formatter.m_forward = false;
    }

    public void visitProject(final CProject project) {
        logger.debug("Visiting {}", project);
        if (project.getProjectParentName() == null) {
            m_projects.add(project); // Append project to projects list
        } else {
            m_subprojects.add(project);  // Append project to subprojects list
        }
    };

    public void visitTask(final CTask task) {
        logger.debug("Visiting {}", task);
        m_tasks.add(task);  // Append task to task list
    };

    public void visitInterval(final CInterval interval) {
        logger.debug("Visiting {}", interval);
        m_intervals.add(interval);  // Append interval to interval list
    };

    public void generateReport() {
        // Header
        m_formatter.appendHeader();
        // Projects
        m_formatter.appendProjectsHeader();
        for (CProject p: m_projects) {
            if (logger.isDebugEnabled()) {
                logger.debug("Visiting {}", p.getName());
            }
            p.Accept(m_formatter);

        }
        m_formatter.appendLineSeparator();
        // Subprojects
        m_formatter.appendSubprojectsHeader();
        for (CProject p: m_subprojects) {
            if (logger.isDebugEnabled()) {
                logger.debug("Visiting {}", p.getName());
            }
            p.Accept(m_formatter);
        }
        m_formatter.appendLineSeparator();
        // Tasks
        m_formatter.appendTasksHeader();
        for (CTask t: m_tasks) {
            if (logger.isDebugEnabled()) {
                logger.debug("Visiting {}", t.getName());
            }
            t.Accept(m_formatter);
        }
        m_formatter.appendLineSeparator();
        // Intervals
        m_formatter.appendIntervalsHeader();
        for (CInterval i: m_intervals) {
            if (logger.isDebugEnabled()) {
                logger.debug("Visiting {}", i.getName());
            }
            i.Accept(m_formatter);
        }
        m_formatter.appendLineSeparator();

        m_formatter.generateReport();
    };

    protected Vector<CProject> m_projects = new Vector<>();
    protected Vector<CProject> m_subprojects = new Vector();
    protected Vector<CTask> m_tasks = new Vector();
    protected Vector<CInterval> m_intervals = new Vector();
    private static Logger logger = LoggerFactory.getLogger(CVisitorReporterDetailed.class);

}
