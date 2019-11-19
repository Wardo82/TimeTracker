package core.ds.TimeTracker;

import java.util.Date;

/**
 * Visitor Subclass of <code>CVisitorFormatter</code> that implements
 * the text format.
 */
public class CVisitorFormatterText extends CVisitorFormatter {

    /** Initialize start and end of the period for this report.
     * @param start Start of the report period (left bound).
     * @param end End of the report period (right bound). */
    public CVisitorFormatterText(final long start, final long end) {
        super(start, end); // Calls superclass constructor
    }

    public void visitProject(final CProject project) {
        long start = project.getStartTime(); // Get project initial time
        if (m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = project.getEndTime(); // Get project finished time
        if (m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        String parent = "";
        if (project.getProjectParentName() != null) { // If it's not root project
            parent = project.getProjectParentName() + " ";
        }

        m_document = m_document + project.getName() + " " + parent
                + Day.format(new Date(start)) + ", "
                + hour.format(new Date(start)) + " "
                + Day.format(new Date(end)) + ", "
                + hour.format(new Date(end)) + " "
                + duration.format(
                        new Date(project.getTotalTimeWithin(m_startTime, m_endTime)))
                + "\n";
    };

    public void visitTask(final CTask task) {
        long start = task.getStartTime(); // Get project initial time
        if (m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = task.getEndTime(); // Get project finished time
        if (m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        m_document = m_document + task.getName() + " "
                + task.getProjectParentName() + " "
                + Day.format(new Date(start)) + ", "
                + hour.format(new Date(start)) + " "
                + Day.format(new Date(end)) + ", "
                + hour.format(new Date(end)) + " "
                + duration.format(
                        new Date(task.getTotalTimeWithin(m_startTime, m_endTime)))
                + "\n";
    };

    public void visitInterval(final CInterval interval) {
        long start = interval.getStartTime(); // Get project initial time
        if (m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = interval.getEndTime(); // Get project finished time
        if (m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        m_document = m_document + interval.getTaskParentName() + " "
                + interval.getProjectName() + " "
                + interval.getName() + " "
                + Day.format(new Date(start)) + ", "
                + hour.format(new Date(start)) + " "
                + Day.format(new Date(end)) + ", "
                + hour.format(new Date(end)) + " "
                + duration.format(
                        new Date(interval.getTotalTimeWithin(m_startTime, m_endTime)))
                + "\n";
    };


    @Override
    public void appendLineSeparator() {
        m_document = m_document
                + "-------------------------------------------------------------------------\n";
    }

    @Override
    public void appendHeader() {
        appendLineSeparator(); // Line
        m_document = m_document + "Report\n"; // Main title
        appendLineSeparator(); // Line
        m_document = m_document + "Period:\n";
        m_document = m_document + "Since: "
                + Day.format(new Date(m_startTime)) + "\n";
        m_document = m_document + "Until: "
                + Day.format(new Date(m_endTime)) + "\n";
        m_document = m_document + "Current Date: "
                + Day.format(new Date(m_currentTime)) + "\n";
        appendLineSeparator(); // Line
    }

    @Override
    public void appendProjectsHeader() {
        m_document = m_document + "First level projects: \n";
        m_document = m_document + "Name |  Start date  |"
                + "  Finish date  |  Total Time  \n";
    }

    @Override
    public void appendSubprojectsHeader() {
        m_document = m_document + "Sub-projects: \n";
        m_document = m_document + "Name |  Belongs to  |"
                + "  Start date  |  Finish date  |  Total Time  \n";
    }

    @Override
    public void appendTasksHeader() {
        m_document = m_document + "Tasks: \n";
        m_document = m_document + "Name | Parent project  |"
                + "  Start date  |  Finish date  |  Total Time  \n";
    }

    @Override
    public void appendIntervalsHeader() {
        m_document = m_document + "Intervals: \n";
        m_document = m_document + "Name | In task  |  ID  |"
                + "  Start date  |  Finish date  |  Total Time  \n";
    }

    @Override
    public void generateReport() {
        System.out.println(m_document);
    }

    /** String used to compile the whole document before generating
     * the report. */
    private String m_document = "";
}
