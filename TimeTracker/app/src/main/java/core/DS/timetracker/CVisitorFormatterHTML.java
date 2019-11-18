package core.ds.TimeTracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import static j2html.TagCreator.*;

public class CVisitorFormatterHTML extends CVisitorFormatter {

    public CVisitorFormatterHTML(long start, long end) { // Constructor with parameters
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
        ContainerTag tr = tr();
        if(project.getProjectParentName() != null) { // If it's not root project
            parent = project.getProjectParentName() + " ";
            m_subProjectsTable.with(tr(
                    td(project.getName()),
                    td(parent),
                    td(Day.format(new Date(start)) + " " + hour.format(new Date(start)) ),
                    td(Day.format(new Date(end)) + " " + hour.format(new Date(end)) ),
                    td( duration.format(new Date(project.getTotalTimeWithin(m_startTime, m_endTime)))
                    )));
        }else{
            m_mainProjectsTable.with(tr(
                    td(project.getName()),
                    td(parent),
                    td(Day.format(new Date(start)) + " " + hour.format(new Date(start)) ),
                    td(Day.format(new Date(end)) + " " + hour.format(new Date(end)) ),
                    td( duration.format(new Date(project.getTotalTimeWithin(m_startTime, m_endTime)))
                    )));
        }
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

        ContainerTag tr = tr();
        m_tasksTable.with(tr(
                td(task.getName()),
                td(task.getProjectParentName()),
                td(Day.format(new Date(start)) + " " + hour.format(new Date(start))),
                td(Day.format(new Date(end)) + " " + hour.format(new Date(end))),
                td( duration.format(new Date(task.getTotalTimeWithin(m_startTime, m_endTime)))
                )));
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

        ContainerTag tr = tr();
        m_intervalsTable.with(tr(
                td(interval.getTaskParentName()),
                td(interval.getProjectName()),
                td(interval.getName()),
                td(Day.format(new Date(start)) + " " + hour.format(new Date(start))),
                td(Day.format(new Date(end)) + " " + hour.format(new Date(end))),
                td( duration.format(new Date(interval.getTotalTimeWithin(m_startTime, m_endTime)))
                )));
    };

    @Override
    public void printLineSeparator() {
        m_body.with( hr() );
    }

    @Override
    public void printHeader() {
        m_head.withTitle("Report");
        m_htmlDocument.with(m_head);

        m_htmlDocument.with(m_body); // Start body generation

        printLineSeparator(); // Line
        m_body.with(h1("Report")); // Main title
        printLineSeparator(); // Line
        m_body.with(h2("Period:")); // Main title

        ContainerTag headerTable = table();
        headerTable.attr("border",1);
        headerTable.with(th(""));
        headerTable.with(th("Data"));
        ContainerTag tr = tr();
        headerTable.with(tr(td("Since"), td( Day.format(new Date(m_startTime)) )));
        headerTable.with(tr(td("Until"), td( Day.format(new Date(m_endTime)) )));
        headerTable.with(tr(td("Current Date"), td( Day.format(new Date(m_currentTime)) )));

        m_body.with(headerTable); // Apend to document body
        printLineSeparator(); // Line
    }

    @Override
    public void printProjectsHeader() {
        m_body.with( h2("First level projects: ") );
        m_mainProjectsTable.attr("border",1);
        m_mainProjectsTable.with(th("Name"));
        m_mainProjectsTable.with(th("Start date"));
        m_mainProjectsTable.with(th("Finish date"));
        m_mainProjectsTable.with(th("Total time"));
    }

    @Override
    public void printSubprojectsHeader() {
        m_body.with(m_mainProjectsTable);

        m_body.with( h2("Subprojects ") );
        m_subProjectsTable.attr("border",1);
        m_subProjectsTable.with(th("Name"));
        m_subProjectsTable.with(th("Belongs to"));
        m_subProjectsTable.with(th("Start date"));
        m_subProjectsTable.with(th("Finish date"));
        m_subProjectsTable.with(th("Total time"));
    }

    @Override
    public void printTasksHeader() {
        m_body.with(m_subProjectsTable);

        m_body.with( h2("Tasks ") );
        m_tasksTable.attr("border",1);
        m_tasksTable.with(th("Name"));
        m_tasksTable.with(th("Parent project"));
        m_tasksTable.with(th("Start date"));
        m_tasksTable.with(th("Finish date"));
        m_tasksTable.with(th("Total time"));
    }

    @Override
    public void printIntervalsHeader() {
        m_body.with(m_tasksTable);

        m_body.with( h2("Intervals ") );
        m_intervalsTable.attr("border",1);
        m_intervalsTable.with(th("Name"));
        m_intervalsTable.with(th("In task"));
        m_intervalsTable.with(th("ID"));
        m_intervalsTable.with(th("Start date"));
        m_intervalsTable.with(th("Finish date"));
        m_intervalsTable.with(th("Total time"));
    }

    @Override
    public void generateReport() {
        m_body.with(m_intervalsTable);

        System.out.println(m_htmlDocument.renderFormatted());
    }

    private ContainerTag m_htmlDocument = html();
    private Tag m_head = head();
    private ContainerTag m_body = body();

    // Tables
    private ContainerTag m_mainProjectsTable = table();
    private ContainerTag m_subProjectsTable = table();
    private ContainerTag m_tasksTable = table();
    private ContainerTag m_intervalsTable = table();

}
