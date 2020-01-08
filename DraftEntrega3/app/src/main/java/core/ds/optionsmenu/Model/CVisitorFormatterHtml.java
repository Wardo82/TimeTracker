package core.ds.optionsmenu.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import static j2html.TagCreator.*;


/**
 * Visitor Subclass of <code>CVisitorFormatter</code> that implements
 * the <code>HTML</code> format.
 */
public class CVisitorFormatterHtml extends CVisitorFormatter {

    /** Initialize start and end of the period for this report.
     * @param start Start of the report period (left bound).
     * @param end End of the report period (right bound). */
    public CVisitorFormatterHtml(final long start, final long end) {
        super(start, end); // Calls superclass constructor
    }

    public void visitProject(final CProject project) {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting {}", project.getName());
        }
        long start = project.getStartTime(); // Get project initial time
        if (m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = project.getEndTime(); // Get project finished time
        if (m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        String parent = "";
        ContainerTag tr = tr();
        // If it's not root project
        if (project.getProjectParent().getName() != null) {
            parent = project.getProjectParent().getName() + " ";
            m_subProjectsTable.with(tr(
                    td(project.getName()), td(parent),
                    td(Day.format(new Date(start)) + " "
                            + hour.format(new Date(start))),
                    td(Day.format(new Date(end)) + " "
                            + hour.format(new Date(end))),
                    td(duration.format(new Date(
                            project.getTotalTimeWithin(m_startTime, m_endTime)))
                    )));
        } else {
            m_mainProjectsTable.with(tr(
                    td(project.getName()),
                    td(Day.format(new Date(start)) + " "
                            + hour.format(new Date(start))),
                    td(Day.format(new Date(end)) + " "
                            + hour.format(new Date(end))),
                    td(duration.format(new Date(
                            project.getTotalTimeWithin(m_startTime, m_endTime)))
                    )));
        }
    }

    public void visitTask(final CTask task) {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting {}", task.getName());
        }
        long start = task.getStartTime(); // Get project initial time
        if (m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = task.getEndTime(); // Get project finished time
        if (m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        ContainerTag tr = tr();
        m_tasksTable.with(tr(
                td(task.getName()), td(task.getProjectParent().getName()),
                td(Day.format(new Date(start)) + " "
                        + hour.format(new Date(start))),
                td(Day.format(new Date(end)) + " "
                        + hour.format(new Date(end))),
                td(duration.format(new Date(
                            task.getTotalTimeWithin(m_startTime, m_endTime)))
                )));
    }

    public void visitInterval(final CInterval interval) {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting {}", interval.getName());
        }
        long start = interval.getStartTime(); // Get project initial time
        if (m_startTime > start) { // If the project is previous to the report
            start = m_startTime;
        }

        long end = interval.getEndTime(); // Get project finished time
        if (m_endTime < end) { // If the project is not finished
            end = m_endTime;
        }

        ContainerTag tr = tr();
        m_intervalsTable.with(tr(
                td(interval.getTaskParentName()),
                td(interval.getProjectName()),
                td(interval.getName()),
                td(Day.format(new Date(start)) + " "
                        + hour.format(new Date(start))),
                td(Day.format(new Date(end)) + " "
                        + hour.format(new Date(end))),
                td(duration.format(new Date(
                            interval.getTotalTimeWithin(m_startTime,
                                    m_endTime)))
                )));
    }

    @Override
    public void appendLineSeparator() {
        m_body.with(hr());
    }

    @Override
    public void appendHeader() {
        m_head.withTitle("Report");
        m_htmlDocument.with(m_head);

        m_htmlDocument.with(m_body); // Start body generation

        appendLineSeparator(); // Line
        m_body.with(h1("Report")); // Main title
        appendLineSeparator(); // Line
        m_body.with(h2("Period:")); // Main title

        ContainerTag headerTable = table();
        headerTable.attr("border", 1);
        headerTable.with(th(""));
        headerTable.with(th("Data"));
        ContainerTag tr = tr();
        headerTable.with(tr(td("Since"),
                            td(Day.format(new Date(m_startTime)))));
        headerTable.with(tr(td("Until"),
                            td(Day.format(new Date(m_endTime)))));
        headerTable.with(tr(td("Current Date"),
                            td(Day.format(new Date(m_currentTime)))));

        m_body.with(headerTable); // Append to document body
        appendLineSeparator(); // Line
    }

    @Override
    public void appendProjectsHeader() {
        m_body.with(h2("First level projects: "));
        m_mainProjectsTable.attr("border", 1);
        m_mainProjectsTable.with(th("Name"));
        m_mainProjectsTable.with(th("Start date"));
        m_mainProjectsTable.with(th("Finish date"));
        m_mainProjectsTable.with(th("Total time"));
    }

    @Override
    public void appendProjectsData() {
        m_body.with(m_mainProjectsTable);
    }

    @Override
    public void appendSubProjectsHeader() {
        m_body.with(h2("Sub-projects "));
        m_subProjectsTable.attr("border", 1);
        m_subProjectsTable.with(th("Name"));
        m_subProjectsTable.with(th("Belongs to"));
        m_subProjectsTable.with(th("Start date"));
        m_subProjectsTable.with(th("Finish date"));
        m_subProjectsTable.with(th("Total time"));
    }

    @Override
    public void appendSubProjectsData() {
        m_body.with(m_subProjectsTable);
    }

    @Override
    public void appendTasksHeader() {
        m_body.with(h2("Tasks "));
        m_tasksTable.attr("border", 1);
        m_tasksTable.with(th("Name"));
        m_tasksTable.with(th("Parent project"));
        m_tasksTable.with(th("Start date"));
        m_tasksTable.with(th("Finish date"));
        m_tasksTable.with(th("Total time"));
    }

    @Override
    public void appendTasksData() {
        m_body.with(m_tasksTable);
    }

    @Override
    public void appendIntervalsHeader() {
        m_body.with(h2("Intervals "));
        m_intervalsTable.attr("border", 1);
        m_intervalsTable.with(th("Name"));
        m_intervalsTable.with(th("In task"));
        m_intervalsTable.with(th("ID"));
        m_intervalsTable.with(th("Start date"));
        m_intervalsTable.with(th("Finish date"));
        m_intervalsTable.with(th("Total time"));
    }

    @Override
    public void appendIntervalsData() {
        m_body.with(m_intervalsTable);
    }

    @Override
    public void generateReport() {
        System.out.println(m_htmlDocument.renderFormatted());
    }

    /** Components used to compile the whole document before generating
     * the report. */
    private ContainerTag m_htmlDocument = html();
    /** The HTML <code>header</code>. */
    private Tag m_head = head();
    /** The <code>body</code>. */
    private ContainerTag m_body = body();

    // Tables for each important chunk of information.
    private ContainerTag m_mainProjectsTable = table();
    private ContainerTag m_subProjectsTable = table();
    private ContainerTag m_tasksTable = table();
    private ContainerTag m_intervalsTable = table();

    private static Logger logger = LoggerFactory.getLogger(CVisitorFormatterHtml.class);
}
