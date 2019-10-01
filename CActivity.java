package core.DS.timetracker;

public abstract class CActivity {
    /*
    * CActivity: Abstract class for which CProjects and CTasks will be used. The Time Tracker Engine
    * will work on a list of activities.
    * */

/* Methods */
    public String getID() { return m_ID; }

    public String getName() { return m_name; }

    public String getDescription() { return m_description; }


/* Properties */
    private String m_ID;
    private String m_name;
    private String m_description;

}
