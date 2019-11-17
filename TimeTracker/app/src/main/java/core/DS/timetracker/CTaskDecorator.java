package core.ds.TimeTracker;

public class CTaskDecorator extends CTask {
    /**
     * CTaskDecorator: Class used under the decorator pattern to extend functionality and capabilities
     * to be able to have different type of tasks. This function is to be inherited by the child classes
     * that implement the additional functionality
     * **/

    public CTaskDecorator(CTask task) { // Decorator constructor with CTask as parameter
        this.m_task =  task;
        m_startTime = task.getStartTime();
    }

    /*
    * The decorator passes the request to the CTask property.
    * */
    @Override
    public String getName() {
        return this.m_task.getName();
    }

    @Override
    public long getStartTime() {
        return this.m_task.getStartTime();
    }

    @Override
    public void trackTaskStart() {
        this.m_task.trackTaskStart();
    }

    @Override
    public void trackTaskStop() {
        this.m_task.trackTaskStop();
    }

    @Override
    public long getCurrentTime() {
        return this.m_task.getCurrentTime();
    }

    @Override
    public long getTotalTime() {
        return this.m_task.getTotalTime();
    }


    protected CTask m_task;
}
