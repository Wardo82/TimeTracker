package core.ds.optionsmenu.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Date;

import core.ds.optionsmenu.Adapters.CActivityHorizontalRecyclerAdapter;
import core.ds.optionsmenu.Adapters.CActivityListAdapter;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CProject;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.Model.CTimeTrackerEngine;
import core.ds.optionsmenu.Model.CTimeTrackerEngine2;
import core.ds.optionsmenu.R;
import core.ds.optionsmenu.View.NonScrollListView;

public class ProjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();

    /**
     * Drawer menu that appears from left to right.
     */
    private DrawerLayout m_drawerMenu;
    private Toolbar m_toolbar;

    private CProject m_project;

    // Variables of the UI
    private TextView labelName;
    private TextView labelParentProject;
    private TextView labelDescription;
    private TextView labelType;
    private TextView timeStart;
    private TextView timeDuration;
    private TextView timeEnd;
    private ImageButton isTrackingButton;
    private NonScrollListView m_activityListView;
    private CActivityListAdapter m_activityListAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cactivity_view_layout);

        // Get the view element from the UI through its id
        labelName = findViewById(R.id.labelName);
        labelDescription = findViewById(R.id.labelDescription);
        labelParentProject = findViewById(R.id.labelParentProject);
        labelType = findViewById(R.id.labelType);
        isTrackingButton = findViewById(R.id.isTrackingButton);
        timeStart = findViewById(R.id.timeStart);
        timeDuration = findViewById(R.id.timeDuration);
        timeEnd = findViewById(R.id.timeEnd);

        // Project of the activity
        m_project = new CProject("", "");
        // Array of high level projects of the application
        ArrayList<CActivity> activityList = new ArrayList<>();

        // Get the main horizontal recycler view, adapter and layout manager.
        m_activityListView = findViewById(R.id.listView);
        m_activityListAdapter = new CActivityListAdapter(
                this,
                R.layout.cactivity_item_view_layout,
                activityList);

        // Apply the adapter to the list view.
        m_activityListView.setAdapter(m_activityListAdapter);
        m_activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(final AdapterView<?> parent,
                                    final View view,
                                    final int position,
                                    final long id) {
                // Get activity from the project's children
                CActivity activity = activityList.get(position);
                // Create the intent for the corresponding activity
                Intent intent = new Intent(ProjectActivity.LOWER_LEVEL);
                intent.putExtra("CLICKED_ACTIVITY_NAME", activity.getName());
                intent.putExtra("PROJECT_NAME", m_project.getName());
                sendBroadcast(intent);
                if (activity instanceof CProject) {
                    // Present the project activity. In it the children will
                    // be requested.
                    startActivity(new Intent(ProjectActivity.this,
                            ProjectActivity.class));
                } else if (activity instanceof CTask) {
                    // Present the task activity. The intervals will be
                    // requested there.
                    startActivity(new Intent(ProjectActivity.this,
                            TaskActivity.class));
                } else {
                    assert false : "Activity is either project nor task.";
                }
                Toast.makeText(ProjectActivity.this,
                        "You clicked on " + activity.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        m_toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(m_toolbar);

        m_drawerMenu = findViewById(R.id.activityToolbar);
        NavigationView navigationView = findViewById(R.id.conf_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this,
                        m_drawerMenu, m_toolbar,
                        R.string.navigation_configuration_open,
                        R.string.navigation_configuration_close);
        m_drawerMenu.addDrawerListener(toggle);
        toggle.syncState();
        m_toolbar.setNavigationIcon(R.drawable.ic_home);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(final SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(
                        0xFF,
                        0x00,
                        0x00)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_clear);
                // add to menu
                menu.addMenuItem(deleteItem);

                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(
                        0x00,
                        0xFF,
                        0x00)));
                // set item width
                editItem.setWidth(170);
                // set item title
                editItem.setIcon(R.drawable.ic_edit);
                // add to menu
                menu.addMenuItem(editItem);
            }
        };
        m_activityListView.setMenuCreator(creator);
        m_activityListView.setOnMenuItemClickListener(
                new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position,
                                           final SwipeMenu menu,
                                           final int index) {
                Log.d(TAG, "Pos: " + position
                        + " menu: " + menu
                        + " index: " + index);
                // Get activity from the project's children
                CActivity activity = activityList.get(position);
                // Intent to send
                Intent intent;
                // Decide what to do based on clicked item
                switch (index) {
                    case 0:
                        intent = new Intent(ProjectActivity.ERASE_ELEMENT);
                        intent.putExtra("SWIPE_ACTIVITY_NAME",
                                        activity.getName());
                        intent.putExtra("PROJECT_NAME", m_project.getName());
                        sendBroadcast(intent);
                        m_activityListAdapter.remove(position);
                        break;
                    case 1:
                        intent = new Intent(ProjectActivity.this,
                                EditActivity.class);
                        intent.putExtra("EDIT_ACTIVITY_NAME",
                                        activity.getName());
                        intent.putExtra("EDIT_ACTIVITY_DESCRIPTION",
                                        activity.getDescription());
                        intent.putExtra("PROJECT_NAME", m_project.getName());
                        startActivity(intent);
                        break;
                        default:
                            break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Intent to send
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.languageOption:
                Toast.makeText(this,"Language", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reportOption:
                intent = new Intent(ProjectActivity.this,
                        ReportActivity.class);
                startActivity(intent);
                Toast.makeText(this,"Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.orderbyOption:
                Toast.makeText(this,"Order By", Toast.LENGTH_SHORT).show();
                break;
        }
        m_drawerMenu.closeDrawer(GravityCompat.START);
        return true; //return true means no item selected
    }

    /**
     * String that defines the action of asking the
     * <code>CTimeTrackerEngine2</code> the list of high level projects.
     * This will arrive as data extra of an Intent with the action HAS_CHILDREN.
     * @see CTimeTrackerEngine2
     */
    public static final String GIVE_CHILDREN = "Give_children";

    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> that parent project of the current activities is the father
     * of the previous parent activity.
     */
    public static final String UPPER_LEVEL = "Upper_level";
    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> that parent project of the current activities is the one that
     * the user clicked.
     */
    public static final String LOWER_LEVEL = "Lower_level";
    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> to stop the chronometer.
     */
    public static final String STOP_CHRONO = "Stop_chrono";
    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> to start the chronometer.
     */
    public static final String START_CHRONO = "Start_chrono";
    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> to erase an activity.
     */
    public static final String ERASE_ELEMENT = "Erase_element";
    /**
     * Receives the Intents that <code>CTimeTrackerEngine2</code> sends
     * with the data of the activities to show.
     */
    private class Receiver extends BroadcastReceiver {
        /**
         * Name of the class to identify when logging or message passing.
         * @see Log
         */
        private final String tag = this.getClass().getCanonicalName();

        /**
         * Handles all Intents sent. It consists on updating the data list
         * that is being shown through the adapter.
         * @param context Context
         * @param intent Object that arrives through Broadcast and which
         *               we make use of the attribute action to know what
         *               type of Intent it is and the extras to get important
         *               data.
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.i(tag, "onReceive of "
                    + intent.getAction());
            if (intent.getAction().equals(CTimeTrackerEngine2.HAS_CHILDREN)) {
                m_isCurrentParentRoot = intent.getBooleanExtra(
                        "current_activity_is_root", false);

                CActivity act = (CActivity) intent
                        .getSerializableExtra("ACTIVITY");
                m_project = (CProject) act;
                m_toolbar.setTitle("");

                // Set the text to the corresponding task information
                labelName.setText(m_project.getName());
                labelDescription.setText(m_project.getDescription());
                labelParentProject.setText(m_project
                        .getProjectParent().getName());
                labelType.setText("Project");
                timeStart.setText(
                        String.format("%s, %s",
                                CTimeTrackerEngine.getInstance()
                                .Day.format(new Date(m_project.getStartTime())),
                                CTimeTrackerEngine.getInstance()
                                .hour.format(new Date(m_project.getStartTime()))
                        ));
                timeDuration.setText(
                        String.format("%s",
                                CTimeTrackerEngine.getInstance()
                                .duration.format(new Date(
                                        m_project.getTotalTime()))
                        ));
                timeEnd.setText(
                        String.format("%s, %s",
                                CTimeTrackerEngine.getInstance()
                                .Day.format(new Date(m_project.getEndTime())),
                                CTimeTrackerEngine.getInstance()
                                .hour.format(new Date(m_project.getEndTime()))
                        ));
                if (m_project.isTracked()) {
                    isTrackingButton.setVisibility(View.VISIBLE);
                } else {
                    isTrackingButton.setVisibility(View.INVISIBLE);
                }
                @SuppressWarnings("unchecked")
                ArrayList<CActivity> activities =
                        (ArrayList<CActivity>) intent
                                .getSerializableExtra("activity_list");
                m_activityListAdapter.clear();
                for (CActivity dadesAct : activities) {
                    m_activityListAdapter.add(dadesAct);
                }
                // Redraw the list
                m_activityListAdapter.notifyDataSetChanged();
                Log.d(TAG, "Show the updated projects");
            } else {
                assert false : "Non implemented Intent action";
            }
        }
    }

    /**
     * Only object of the class {@link Receiver}.
     */
    private Receiver m_receiver;
    /**
     * When the activity is presented again, the receiver and its
     * filter should attend to Intents that are broadcasted. Also start the
     * <code>TODO</code> if its the first time this activity is shown.
     */
    @Override
    public final void onResume() {
        Log.i(TAG, "onResume");

        IntentFilter filter;
        filter = new IntentFilter();
        filter.addAction(CTimeTrackerEngine2.HAS_CHILDREN);
        m_receiver = new Receiver();
        registerReceiver(m_receiver, filter);
        // Starts the service activity CTimeTrackerEngine2
        // if it was not created.
        startService(new Intent(this, CTimeTrackerEngine2.class));

        super.onResume();
        Log.i(TAG, "End of onResume");
    }
    /**
     * Right before hiding this activity for another, we cancel the receiver
     * of Intents.
     */
    @Override
    public final void onPause() {
        Log.i(TAG, "onPause");
        unregisterReceiver(m_receiver);
        super.onPause();
    }
    /**
     * Menu icons are inflated just as they were with actionbar.
     * This function works for the burger menu.
     * @param menu The options menu
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
    /**
     * Override onOptionsItemSelected, so that whenever they tap on any of those
     * menu items we do a different function.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        // Get activity from the project's children
        CActivity activity = m_project;
        // Intent to send
        Intent intent;
        // Decide what to do based on clicked item
        switch (id){
            case R.id.addOption:
                intent = new Intent(ProjectActivity.this,
                        AddActivity.class);
                startActivity(intent);
                Toast.makeText(ProjectActivity.this,
                        getString(R.string.toast_add_new_activity),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.editOption:
                intent = new Intent(ProjectActivity.this,
                        EditActivity.class);
                intent.putExtra("EDIT_ACTIVITY_NAME",
                        activity.getName());
                intent.putExtra("EDIT_ACTIVITY_DESCRIPTION",
                        activity.getDescription());
                intent.putExtra("PROJECT_NAME",
                        activity.getName());
                startActivity(intent);
                Toast.makeText(ProjectActivity.this,
                        item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteOption:
                intent = new Intent(ProjectActivity.ERASE_ELEMENT);
                intent.putExtra("SWIPE_ACTIVITY_NAME",
                        activity.getName());
                intent.putExtra("PROJECT_NAME", activity.getName());
                sendBroadcast(intent);
                Toast.makeText(ProjectActivity.this,
                        item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.backButton:
                Toast.makeText(ProjectActivity.this,
                        "Back to " + m_project.getProjectParent().getName(),
                        Toast.LENGTH_SHORT).show();
                // If the parent project is root (It has no parent)
                if (m_project.getProjectParent().getProjectParent().isRoot()) {
                    sendBroadcast(new Intent(ProjectActivity.UPPER_LEVEL));
                    sendBroadcast(new Intent(ProjectActivity.UPPER_LEVEL));
                    Log.d(TAG, "Sending UPPER_LEVEL intent.");
                    sendBroadcast(new Intent(ProjectActivity.GIVE_CHILDREN));
                    Log.d(TAG, "Sending GIVE_CHILDREN intent.");
                    startActivity(new Intent(ProjectActivity.this,
                            MainActivity.class));
                    super.onBackPressed();
                } else {
                    sendBroadcast(new Intent(ProjectActivity.UPPER_LEVEL));
                    Log.d(TAG, "Sending UPPER_LEVEL intent.");
                    sendBroadcast(new Intent(ProjectActivity.GIVE_CHILDREN));
                    Log.d(TAG, "Sending GIVE_CHILDREN intent.");
                }
                Toast.makeText(ProjectActivity.this,
                        "All task in this activity will be paused",
                        Toast.LENGTH_LONG).show();
                // TODO: pausar todo
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Hanlder of back button on the D-pad. GO up on the tree of
     * tasks and projects.
     * If there is a parent project we go up to show it's elements.
     * If there is none, we stop the service, save and close the app.
     */
    @Override
    public final void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        // If the parent project is root (It has no parent)
        if (m_project.getProjectParent().getProjectParent().isRoot()) {
            sendBroadcast(new Intent(ProjectActivity.UPPER_LEVEL));
            sendBroadcast(new Intent(ProjectActivity.UPPER_LEVEL));
            Log.d(TAG, "Sending UPPER_LEVEL intent.");
            sendBroadcast(new Intent(ProjectActivity.GIVE_CHILDREN));
            Log.d(TAG, "Sending GIVE_CHILDREN intent.");
            startActivity(new Intent(ProjectActivity.this,
                    MainActivity.class));
            super.onBackPressed();
        } else {
            sendBroadcast(new Intent(ProjectActivity.UPPER_LEVEL));
            Log.d(TAG, "Sending UPPER_LEVEL intent.");
            sendBroadcast(new Intent(ProjectActivity.GIVE_CHILDREN));
            Log.d(TAG, "Sending GIVE_CHILDREN intent.");
        }
    }

    /**
     * Flag that helps us choose the activity to present.
     */
    private boolean m_isCurrentParentRoot;
}
