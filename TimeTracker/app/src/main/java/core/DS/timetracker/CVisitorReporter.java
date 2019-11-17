package core.ds.TimeTracker;

import java.util.Vector;

public abstract class CVisitorReporter extends CVisitor {

    public CVisitorReporter(String format, long start, long end) {
        switch (format) {
            case "text":
                m_formatter = new CVisitorFormatterText(start, end);
                break;
            case "HTML":
                m_formatter = new CVisitorFormatterHTML(start, end);
                break;
            default:
                System.out.println("Wrong format or Format not supported.");
        }
    }

    public abstract void visitProject(CProject project);
    public abstract void visitTask(CTask task);
    public abstract void visitInterval(CInterval interval);

    public abstract void generateReport();

    public CVisitorFormatter getFormatter() {
        return m_formatter;
    }

    public void setFormatter(CVisitorFormatter m_formatter) {
        this.m_formatter = m_formatter;
    }

    /* public void printHeader() {
        this.m_formatter.printHeader();
    } */

    protected CVisitorFormatter m_formatter;
}
