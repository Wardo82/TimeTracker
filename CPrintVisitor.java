package core.DS.timetracker;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CPrintVisitor extends CVisitor {

    SimpleDateFormat Day = new SimpleDateFormat("d-M-YY hh:mm:ss");
    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");

    public void visitProject(CProject project){
        System.out.println( project.getName()+" "+
                        Day.format(new Date(project.getStartTime()))+ " "+
                        Day.format(new Date(project.getCurrentTime()))+" "+
                        ft.format(new Date(project.getTotalTime())) );

        // TODO: Why not project.print() o en ambos casos activity.print()
    }

    public void visitTask(CTask task){
        System.out.println( task.getName()+" "+
                        Day.format(new Date(task.getStartTime()) )+" "+
                        Day.format(new Date(task.getCurrentTime()) )+" "+
                        ft.format(new Date(task.getTotalTime()) ) );
    }

    public void visitInterval(CInterval interval) {

    }

}
