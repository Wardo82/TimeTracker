package core.ds.optionsmenu.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Visitor class that prints information about activities.
 */
public class CVisitorPrint extends CVisitor {

    public void visitProject(final CProject project) {
        System.out.println(project.getName() + " "
                + Day.format(new Date(project.getStartTime())) + " "
                + Day.format(new Date(project.getCurrentTime())) + " "
                + ft.format(new Date(project.getTotalTime())));

        // TODO Why not project.print() or activity.print()?.
    }

    public void visitTask(final CTask task) {
        System.out.println(task.getName() + " "
                + Day.format(new Date(task.getStartTime())) + " "
                + Day.format(new Date(task.getCurrentTime())) + " "
                + ft.format(new Date(task.getTotalTime())));
    }

    public void visitInterval(final CInterval interval) {
        // Do nothing
    }

    private SimpleDateFormat Day = new SimpleDateFormat("d-M-YY hh:mm:ss", Locale.US);
    private SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss", Locale.US);

}
