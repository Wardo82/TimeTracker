package core.ds.optionsmenu.Model;

import android.util.Log;
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

    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();

    public CVisitorReporterBrief(final CVisitorFormatter formatter) { // Constructor with parameters
        super(formatter); // Calls superclass constructor
        m_forward = false;
        m_formatter.m_forward = false;
    }

    public void visitProject(final CProject project) {
        Log.d(TAG, "Visiting: " + project.getName());
        m_projects.add(project); // Append project to projects list
    }

    public void visitTask(final CTask task) { }

    public void visitInterval(final CInterval interval) { }

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
}
