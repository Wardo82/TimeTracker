package core.ds.TimeTracker;

public class CVisitorFormatterHTML extends CVisitorFormatter {

    public CVisitorFormatterHTML(long start, long end) { // Constructor with parameters
        super(start, end); // Calls superclass constructor
    }

    public void visitProject(CProject project){

    };

    public void visitTask(CTask task){

    };

    public void visitInterval(CInterval interval){

    };

    @Override
    public void printHeader() {

    };

    @Override
    public void printLineSeparator() {

    };

    @Override
    public void printProjectsHeader() {

    };

    @Override
    public void printSubprojectsHeader() {

    }

    @Override
    public void printTasksHeader() {

    }

    @Override
    public void printIntervalsHeader() {

    }

}
