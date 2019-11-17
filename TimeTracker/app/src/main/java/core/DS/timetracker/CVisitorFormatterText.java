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

        System.out.println(project.getName() + " " + parent +
                Day.format(new Date(start)) + ", " + hour.format(new Date(start)) + " " +
                Day.format(new Date(end)) + ", " + hour.format(new Date(end)) + " " +
                duration.format(new Date(project.getTotalTimeWithin(m_startTime, m_endTime))));
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

        System.out.println(task.getName() + " " + task.getProjectParentName() + " " +
                Day.format(new Date(start)) + ", " + hour.format(new Date(start)) + " " +
                Day.format(new Date(end)) + ", " + hour.format(new Date(end)) + " " +
                duration.format(new Date(task.getTotalTimeWithin(m_startTime, m_endTime))));
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

        System.out.println(interval.getTaskParentName() + " " + interval.getProjectName() + " " +
                interval.getName() + " " +
                Day.format(new Date(start)) + ", " + hour.format(new Date(start)) + " " +
                Day.format(new Date(end)) + ", " + hour.format(new Date(end)) + " " +
                duration.format(new Date(interval.getTotalTimeWithin(m_startTime, m_endTime))));
    };

    @Override
    public void printLineSeparator() {
        System.out.println("-------------------------------------------------------------------------");
    }

    @Override
    public void printHeader() {
        printLineSeparator(); // Line
        System.out.println("Report"); // Main title
        printLineSeparator(); // Line
        System.out.println("Period:");
        System.out.println("Since: " + Day.format(new Date(m_startTime)) );
        System.out.println("Until: " + Day.format(new Date(m_endTime)) );
        System.out.println("Current Date: " + Day.format(new Date(m_currentTime)));
        printLineSeparator(); // Line
    }

    @Override
    public void printProjectsHeader() {
        System.out.println("First level projects: ");
        System.out.println("Name |  Start date  |  Finish date  |  Total Time  ");
    }

    @Override
    public void printSubprojectsHeader() {
        System.out.println("Sub-projects: ");
        System.out.println("Name |  Belongs to  |  Start date  |  Finish date  |  Total Time  ");
    }

    @Override
    public void printTasksHeader() {
        System.out.println("Tasks: ");
        System.out.println("Name | Parent project  |  Start date  |  Finish date  |  Total Time  ");
    }

    @Override
    public void printIntervalsHeader() {
        System.out.println("Intervals: ");
        System.out.println("Name | In task  |  ID  |  Start date  |  Finish date  |  Total Time  ");
    }

    private SimpleDateFormat Day = new SimpleDateFormat("d/M/YY");
    private SimpleDateFormat hour = new SimpleDateFormat ("hh:mm");
    private SimpleDateFormat duration = new SimpleDateFormat("h'h' m'm' s's'");

}
