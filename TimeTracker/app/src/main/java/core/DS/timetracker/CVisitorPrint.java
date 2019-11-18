package core.ds.TimeTracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CVisitorPrint extends CVisitor {

    public void visitProject(CProject project){
        System.out.println( project.getName()+" "+
                        Day.format(new Date(project.getStartTime()))+ " "+
                        Day.format(new Date(project.getCurrentTime()))+" "+
                        ft.format(new Date(project.getTotalTime())) );

        // TODO: Why not project.print() o en ambos casos activity.print()
    }

    public void visitTask(final CTask task) {
        System.out.println(task.getName()+ " "+
                        Day.format(new Date(task.getStartTime()) )+" "+
                        Day.format(new Date(task.getCurrentTime()) )+" "+
                        ft.format(new Date(task.getTotalTime()) ) );
    }

    public void visitInterval(CInterval interval) {

    }

    private SimpleDateFormat Day = new SimpleDateFormat("d-M-YY hh:mm:ss", Locale.US);
    private SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss", Locale.US);

}
