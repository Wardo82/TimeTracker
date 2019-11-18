package core.ds.TimeTracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CVisitorFormatterText extends CVisitorFormatter {

    public CVisitorFormatterText(long start, long end) { // Constructor with parameters
        super(start, end); // Calls superclass constructor
    }

    public void visitProject(CProject project){
        long start = project.getStartTime(); // Get project initial time
        if(m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = project.getEndTime(); // Get project finished time
        if(m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        String parent = "";
        if(project.getProjectParentName() != null) { // If it's not root project
            parent = project.getProjectParentName() + " ";
        }

        m_document = m_document + project.getName() + " " + parent +
                Day.format(new Date(start)) + ", " + hour.format(new Date(start)) + " " +
                Day.format(new Date(end)) + ", " + hour.format(new Date(end)) + " " +
                duration.format(new Date(project.getTotalTimeWithin(m_startTime, m_endTime))) + "\n";
    };

    public void visitTask(CTask task) {
        long start = task.getStartTime(); // Get project initial time
        if(m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = task.getEndTime(); // Get project finished time
        if(m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        m_document = m_document + task.getName() + " " + task.getProjectParentName() + " " +
                Day.format(new Date(start)) + ", " + hour.format(new Date(start)) + " " +
                Day.format(new Date(end)) + ", " + hour.format(new Date(end)) + " " +
                duration.format(new Date(task.getTotalTimeWithin(m_startTime, m_endTime))) + "\n";
    };

    public void visitInterval(CInterval interval){
        long start = interval.getStartTime(); // Get project initial time
        if(m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = interval.getEndTime(); // Get project finished time
        if(m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        m_document = m_document + interval.getTaskParentName() + " " + interval.getProjectName() + " " +
                interval.getName() + " " +
                Day.format(new Date(start)) + ", " + hour.format(new Date(start)) + " " +
                Day.format(new Date(end)) + ", " + hour.format(new Date(end)) + " " +
                duration.format(new Date(interval.getTotalTimeWithin(m_startTime, m_endTime))) + "\n";
    };

    @Override
    public void printLineSeparator() {
        m_document = m_document + "-------------------------------------------------------------------------\n";
    }

    @Override
    public void printHeader() {
        printLineSeparator(); // Line
        m_document = m_document + "Report\n"; // Main title
        printLineSeparator(); // Line
        m_document = m_document + "Period:\n";
        m_document = m_document + "Since: " + Day.format(new Date(m_startTime)) + "\n";
        m_document = m_document + "Until: " + Day.format(new Date(m_endTime)) + "\n";
        m_document = m_document + "Current Date: " + Day.format(new Date(m_currentTime)) + "\n";
        printLineSeparator(); // Line
    }

    @Override
    public void printProjectsHeader() {
        m_document = m_document + "First level projects: \n";
        m_document = m_document + "Name |  Start date  |  Finish date  |  Total Time  \n";
    }

    @Override
    public void printSubprojectsHeader() {
        m_document = m_document + "Sub-projects: \n";
        m_document = m_document + "Name |  Belongs to  |  Start date  |  Finish date  |  Total Time  \n";
    }

    @Override
    public void printTasksHeader() {
        m_document = m_document + "Tasks: \n";
        m_document = m_document + "Name | Parent project  |  Start date  |  Finish date  |  Total Time  \n";
    }

    @Override
    public void printIntervalsHeader() {
        m_document = m_document + "Intervals: \n";
        m_document = m_document + "Name | In task  |  ID  |  Start date  |  Finish date  |  Total Time  \n";
    }

    @Override
    public void generateReport() {
        System.out.println(m_document);
    }

    private String m_document = "";

}
