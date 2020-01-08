package core.ds.optionsmenu.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used under the decorator pattern to extend functionality and
 * capabilities to be able to have different type of tasks. This class
 * is to be inherited by the child classes that actually implement the
 * additional functionality (see CTaskLimitedTime or CTaskProgrammed).
 * The decorator works as a wrapper, it <i>has a</i> CTask to which the
 * decorator forwards all requests.
 */
public class CTaskDecorator extends CTask {
    /**
     * Decorator constructor with CTask as parameter.
     * @param task The main task at hand that handles all method calls
     */
    public CTaskDecorator(final CTask task) {
        this.m_task =  task;
        m_startTime = task.getStartTime();
    }

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
        try {
            this.m_task.trackTaskStart();
        } catch (Exception e) {
            logger.error("There is another interval running at the moment", e);
        }

    }

    @Override
    public void trackTaskStop() {
        try {
            this.m_task.trackTaskStop();
        } catch (Exception e) {
            logger.error("Empty list of intervals", e);
        }
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
    private static Logger logger = LoggerFactory.getLogger(CTaskDecorator.class);

}
