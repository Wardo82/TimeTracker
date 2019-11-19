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
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting {}", project.getName());
        }
        if (project.getProjectParentName() == null) {
            m_projects.add(project); // Append project to projects list
        } else {
            m_subProjects.add(project);  // Append project to sub-projects list
        }
    };

    public void visitTask(final CTask task) {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting {}", task.getName());
        }
        m_tasks.add(task);  // Append task to task list
    };

    public void visitInterval(final CInterval interval) {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting {}", interval.getName());
        }
        m_intervals.add(interval);  // Append interval to interval list
    };

    public void generateReport() {
        /* Header */
        m_formatter.appendHeader();
        /* Projects */
        m_formatter.appendProjectsHeader();
        for (CProject p: m_projects) {
            p.Accept(m_formatter);

        }
        m_formatter.appendProjectsData();
        m_formatter.appendLineSeparator();
        /* Sub-projects */
        m_formatter.appendSubProjectsHeader();
        for (CProject p: m_subProjects) {
            p.Accept(m_formatter);
        }
        m_formatter.appendSubProjectsData();
        m_formatter.appendLineSeparator();
        /* Tasks */
        m_formatter.appendTasksHeader();
        for (CTask t: m_tasks) {
            t.Accept(m_formatter);
        }
        m_formatter.appendTasksData();
        m_formatter.appendLineSeparator();
        /* Intervals */
        m_formatter.appendIntervalsHeader();
        for (CInterval i: m_intervals) {
            i.Accept(m_formatter);
        }
        m_formatter.appendIntervalsData();
        m_formatter.appendLineSeparator();

        m_formatter.generateReport();
    };

    protected Vector<CProject> m_projects = new Vector<>();
    protected Vector<CProject> m_subProjects = new Vector();
    protected Vector<CTask> m_tasks = new Vector();
    protected Vector<CInterval> m_intervals = new Vector();
    private static Logger logger = LoggerFactory.getLogger(CVisitorReporterDetailed.class);

}
