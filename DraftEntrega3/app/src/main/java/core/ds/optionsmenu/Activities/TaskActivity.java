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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import core.ds.optionsmenu.Adapters.CIntervalListAdapter;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CInterval;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.Model.CTimeTrackerEngine;
import core.ds.optionsmenu.Model.CTimeTrackerEngine2;
import core.ds.optionsmenu.R;
import core.ds.optionsmenu.View.NonScrollListView;

public class TaskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Name of the class to identify the messages from the logging mechanism.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Drawer menu that appears from left to right.
     */
    private DrawerLayout m_drawerMenu;
    private Toolbar m_toolbar;

    // Variables of the UI
    private TextView labelName;
    private TextView labelDescription;
    private TextView labelType;
    private TextView timeStart;
    private TextView timeDuration;
    private TextView timeEnd;
    private NonScrollListView m_intervalListView;
    private CIntervalListAdapter m_intervalListAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cactivity_view_layout);

        // Get the Intent that started this activity and extract the string
        m_task = new CTask("", "");

        // Get the view element from the UI through its id
        labelName = findViewById(R.id.labelName);
        labelDescription = findViewById(R.id.labelDescription);
        labelType = findViewById(R.id.labelType);
        timeStart = findViewById(R.id.timeStart);
        timeDuration = findViewById(R.id.timeDuration);
        timeEnd = findViewById(R.id.timeEnd);

        // Create track time button for task activity
        RelativeLayout myLayout = findViewById(R.id.mainLayout);
        Button trackButton = new Button(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.labelDescription);
        trackButton.setHeight(30);
        trackButton.setId(R.id.button);
        trackButton.setTag("Track button");
        trackButton.setLayoutParams(params);
        trackButton.setText("PLAY");
        myLayout.addView(trackButton);
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i(TAG, "onButtonClick");
                Log.d(TAG, "Clicked view" + v.getTag());
                switch (v.getId()) {
                    case R.id.button:
                        Intent intent;
                        if (!(CTimeTrackerEngine.getInstance().m_trackedTask
                                .containsKey(m_task.getName()))) {
                            trackButton.setText("PAUSE");
                            intent = new Intent(
                                    TaskActivity.START_CHRONO);
                            Toast.makeText(TaskActivity.this,
                                    "Start tracking " + m_task.getName(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            trackButton.setText("PLAY");
                            intent = new Intent(
                                    ProjectActivity.STOP_CHRONO);
                            Toast.makeText(TaskActivity.this,
                                    "Stop tracking " + m_task.getName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        // The name of the clicked item
                        intent.putExtra("CLICKED_TASK",
                                m_task.getName());
                        intent.putExtra("PARENT_NAME",
                                m_task.getProjectParent().getName());
                        sendBroadcast(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        // Array of high level projects of the application
        ArrayList<CInterval> intervalList = new ArrayList<>();

        // Get the main horizontal recycler view, adapter and layout manager.
        m_intervalListView = findViewById(R.id.listView);
        m_intervalListAdapter = new CIntervalListAdapter(
                this,
                R.layout.cinterval_item_view_layout,
                intervalList);

        // Apply the adapter to the list view.
        m_intervalListView.setAdapter(m_intervalListAdapter);

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
            public void create(SwipeMenu menu) {
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
            }
        };
        m_intervalListView.setMenuCreator(creator);
        m_intervalListView.setOnMenuItemClickListener(
                new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position,
                                                   final SwipeMenu menu,
                                                   final int index) {
                        Log.d(TAG, "Pos: " + position
                                + " menu: " + menu
                                + " index: " + index);
                        // Get interval from the project's children
                        CInterval interval = intervalList.get(position);
                        Intent intent = new Intent(TaskActivity.ERASE_ELEMENT);
                        intent.putExtra("SWIPE_ACTIVITY_NAME",
                                interval.getName());
                        intent.putExtra("PROJECT_NAME",
                                m_task.getName());
                        sendBroadcast(intent);
                        m_intervalListAdapter.remove(position);

                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });
    }

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
            Log.i(tag, "onReceive");
            if (intent.getAction().equals(CTimeTrackerEngine2.HAS_CHILDREN)) {
                m_isCurrentParentRoot = intent.getBooleanExtra(
                        "current_activity_is_root", false);

                CActivity act = (CActivity) intent
                        .getSerializableExtra("ACTIVITY");
                m_task = (CTask) act;
                m_toolbar.setTitle(">" + m_task.getName());
                // Set the text to the corresponding task information
                labelName.setText(m_task.getName());
                labelDescription.setText(m_task.getDescription());
                labelType.setText("Task");
                timeStart.setText(
                        String.format("%s, %s",
                                CTimeTrackerEngine.getInstance()
                                .Day.format(new Date(m_task.getStartTime())),
                                CTimeTrackerEngine.getInstance()
                                .hour.format(new Date(m_task.getStartTime()))
                        ));
                timeDuration.setText(
                        String.format("%s",
                                CTimeTrackerEngine.getInstance()
                                        .duration.format(new Date(m_task.getTotalTime()))
                        ));
                timeEnd.setText(
                        String.format("%s, %s",
                                CTimeTrackerEngine.getInstance()
                                        .Day.format(new Date(m_task.getEndTime())),
                                CTimeTrackerEngine.getInstance()
                                        .hour.format(new Date(m_task.getEndTime()))
                        ));

                @SuppressWarnings("unchecked")
                ArrayList<CInterval> intervals =
                        (ArrayList<CInterval>) intent
                                .getSerializableExtra("interval_list");
                m_intervalListAdapter.clear();
                for (CInterval i : intervals) {
                    m_intervalListAdapter.add(i);
                }
                // Redraw the list
                m_intervalListAdapter.notifyDataSetChanged();
                Log.d(tag, "Show the updated projects");
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
        menu.findItem(R.id.addOption)
                .setVisible(false);
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
        switch (id){
            case R.id.backButton:
                Toast.makeText(TaskActivity.this,
                        "Back to " + m_task.getProjectParent().getName(),
                        Toast.LENGTH_SHORT).show();
                // If the parent project is root (It has no parent)
                if (m_task.getProjectParent().getProjectParent().isRoot()) {
                    sendBroadcast(new Intent(TaskActivity.UPPER_LEVEL));
                    sendBroadcast(new Intent(TaskActivity.UPPER_LEVEL));
                    Log.d(TAG, "Sending UPPER_LEVEL intent.");
                    sendBroadcast(new Intent(TaskActivity.GIVE_CHILDREN));
                    Log.d(TAG, "Sending GIVE_CHILDREN intent.");
                    startActivity(new Intent(TaskActivity.this,
                            MainActivity.class));
                } else {
                    sendBroadcast(new Intent(TaskActivity.UPPER_LEVEL));
                    Log.d(TAG, "Sending UPPER_LEVEL intent.");
                    sendBroadcast(new Intent(TaskActivity.GIVE_CHILDREN));
                    Log.d(TAG, "Sending GIVE_CHILDREN intent.");
                }
            case R.id.editOption:
                Toast.makeText(TaskActivity.this,
                        item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteOption:
                Toast.makeText(TaskActivity.this,
                        item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(this,"Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.orderbyOption:
                intent = new Intent(TaskActivity.this,
                        ReportActivity.class);
                startActivity(intent);
                Toast.makeText(this,"Order By", Toast.LENGTH_SHORT).show();
                break;
        }
        m_drawerMenu.closeDrawer(GravityCompat.START);
        return true; //return true means no item selected
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
        if (m_task.getProjectParent().getProjectParent().isRoot()) {
            sendBroadcast(new Intent(TaskActivity.UPPER_LEVEL));
            sendBroadcast(new Intent(TaskActivity.UPPER_LEVEL));
            Log.d(TAG, "Sending UPPER_LEVEL intent.");
            sendBroadcast(new Intent(TaskActivity.GIVE_CHILDREN));
            Log.d(TAG, "Sending GIVE_CHILDREN intent.");
            startActivity(new Intent(TaskActivity.this,
                    MainActivity.class));
        } else {
            sendBroadcast(new Intent(TaskActivity.UPPER_LEVEL));
            Log.d(TAG, "Sending UPPER_LEVEL intent.");
            sendBroadcast(new Intent(TaskActivity.GIVE_CHILDREN));
            Log.d(TAG, "Sending GIVE_CHILDREN intent.");
        }
    }
    /**
     * Flag that helps us choose the activity to present.
     */
    private boolean m_isCurrentParentRoot;
    private CTask m_task;
}
