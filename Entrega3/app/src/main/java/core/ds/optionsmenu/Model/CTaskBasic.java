package core.ds.optionsmenu.Model;

/**
 * Basic task with default task functionality. It adds no new behaviour.
 * Used to comply with the Decorator pattern (see CTaskDecorator).
 */
public class CTaskBasic extends CTask {

    /** Initialize name and description of this activity by calling superclass
     * constructor.
     * @param name The name of the activity.
     * @param description The description of the activity.
     */
    public CTaskBasic(final String name, final String description) {
        super(name, description);
    }
}
