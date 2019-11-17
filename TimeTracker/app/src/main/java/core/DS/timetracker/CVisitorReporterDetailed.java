package core.ds.TimeTracker;

import java.util.Vector;

public class CVisitorReporterDetailed extends CVisitorReporter {

    public CVisitorReporterDetailed(String format, long start, long end) { // Constructor with parameters
        super(format, start, end); // Calls superclass constructor
        m_forward = true;
        m_formatter.m_forward = false;
    }

    public void visitProject(CProject project){
        if(project.getProjectParentName() == null){
            m_projects.add(project); // Append project to projects list
        }else{
            m_subprojects.add(project);  // Append project to subprojects list
        }
    };

    public void visitTask(CTask task){
        m_tasks.add(task);  // Append task to task list
    };

    public void visitInterval(CInterval interval){
        m_intervals.add(interval);  // Append interval to interval list
    };

    public void generateReport() {
        // Header
        m_formatter.printHeader();
        // Projects
        m_formatter.printProjectsHeader();
        for(CProject p: m_projects){
            p.Accept(m_formatter);

        };
        m_formatter.printLineSeparator();
        // Subprojects
        m_formatter.printSubprojectsHeader();
        for(CProject p: m_subprojects){
            p.Accept(m_formatter);
        };
        m_formatter.printLineSeparator();
        // Tasks
        m_formatter.printTasksHeader();
        for(CTask t: m_tasks){
            t.Accept(m_formatter);
        };
        m_formatter.printLineSeparator();
        // Intervals
        m_formatter.printIntervalsHeader();
        for(CInterval i: m_intervals){
            i.Accept(m_formatter);
        };
        m_formatter.printLineSeparator();
    };

    protected Vector<CProject> m_projects = new Vector<>();
    protected Vector<CProject> m_subprojects = new Vector();
    protected Vector<CTask> m_tasks = new Vector();
    protected Vector<CInterval> m_intervals = new Vector();
}
