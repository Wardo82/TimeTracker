package core.ds.TimeTracker;

import java.util.Vector;

public class CVisitorReporterBrief extends CVisitorReporter {

    public CVisitorReporterBrief(String format, long start, long end) { // Constructor with parameters
        super(format, start, end); // Calls superclass constructor
        m_forward = false;
        m_formatter.m_forward = false;
    }

    public void visitProject(CProject project){
        m_projects.add(project); // Append project to projects list
    };

    public void visitTask(CTask task){ };

    public void visitInterval(CInterval interval){ };

    public void generateReport() {
        m_formatter.printHeader();
        m_formatter.printProjectsHeader();
        for(CProject p: m_projects){
            p.Accept(m_formatter);
        };
        m_formatter.printLineSeparator();
    }

    protected Vector<CProject> m_projects = new Vector();

}
