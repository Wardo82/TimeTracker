package core.DS.timetracker;

public abstract class CVisitor {

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);


}
