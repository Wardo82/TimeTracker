package core.ds.optionsmenu.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import core.ds.optionsmenu.Adapters.CActivityHorizontalRecyclerAdapter;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CTimeTrackerEngine2;
import core.ds.optionsmenu.R;

/**
 * Main activity the user is presented with. It consists of an horizontal list
 * with the main projects of the application.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();

    /**
     * Drawer menu that appears from left to right.
     */
    private DrawerLayout m_drawerMenu;
    /**
     * Recycler view used for the horizontal project display.
     */
    private RecyclerView m_projectsListView;
    /**
     * Adapter used to bind data to UI view.
     */
    private CActivityHorizontalRecyclerAdapter m_adapter;
    /**
     * Layout manager used to set the list as horizontal and display
     * it on screen.
     */
    private RecyclerView.LayoutManager m_layoutManager;
    /**
     * List that holds all the high level projects
     */
    private List<CActivity> m_projectList = new ArrayList<>();
    /**
     * Project tracker id to identify which project is on screen.
     */
    private int m_lastVisibleItem = 0;
    /**
     *
     */
    private Toolbar m_toolbar;
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
                // Get the new list of data that comes with the intent
                m_projectList = (ArrayList<CActivity>) intent
                        .getSerializableExtra("activity_list");
                m_adapter.setItemList(m_projectList);
                //m_adapter = new CActivityHorizontalRecyclerAdapter(
                //        m_projectList, MainActivity.this);
                // This will redraw the ListView
                m_adapter.notifyDataSetChanged();
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
     */
    public static final String GIVE_CHILDREN = "Give_children";

    /**
     * String that defines the action of asking the
     * <code>CTimeTrackerEngine2</code> to write the tree to disc.
     */
    public static final String SAVE_TREE = "Save_tree";

    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> that parent project of the current activities is the one that
     * the user clicked.
     */
    public static final String LOWER_LEVEL = "Lower_level";

    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> that parent project of the current activities is the father
     * of the previous parent activity.
     */
    public static final String UPPER_LEVEL = "Upper_level";

    /**
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> to leave the app.
     */
    public static final String STOP_SERVICE = "Stop_service";

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
     * String that defines the action of asking the <code>CTimeTrackerEngine2
     * </code> to erase an activity.
     */
    public static final String ADD_ACTIVITY = "Add_activity";
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
        // Starts the service activity CTimeTrackerEngine2 if it was not created.
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
     * Class called when this activity is going to be presented.
     * @param savedInstanceState S
     */
    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        sendBroadcast(new Intent(MainActivity.GIVE_CHILDREN));
        // Get the main horizontal recycler view, adapter and layout manager.
        m_projectsListView = findViewById(R.id.mainProjectsListView);
        // Get the new list of data that comes with the intent
        m_adapter = new CActivityHorizontalRecyclerAdapter(
                m_projectList, MainActivity.this);

        m_layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        // Apply the adapter and the layout manager to the recycler view.
        m_projectsListView.setLayoutManager(m_layoutManager);
        m_projectsListView.setAdapter(m_adapter);

        m_toolbar = findViewById(R.id.toolbar);
        m_toolbar.setTitle("");
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

        m_projectsListView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
           @Override
           public void onScrolled(final @NonNull RecyclerView recyclerView,
                                  final int dx, final int dy) {
               m_lastVisibleItem = ((LinearLayoutManager) m_layoutManager)
                           .findLastVisibleItemPosition();
                   Log.d(TAG, "Position: " + m_lastVisibleItem);
           }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.languageOption:
                Toast.makeText(this,"Language", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reportOption:
                intent = new Intent(MainActivity.this,
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
     * Let’s create options menu by overriding onCreateOptionsMenu and
     * inflating the menu file that we created earlier.
     * @param menu The menu to inflate
     * @return True on success
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        menu.findItem(R.id.backButton)
                .setVisible(false);
        return super.onCreateOptionsMenu(menu);
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
        CActivity activity = m_projectList.get(m_lastVisibleItem);
        // Intent to send
        Intent intent;
        // Decide what to do based on clicked item
        switch (id){
            case R.id.addOption:
                intent = new Intent(MainActivity.this,
                    AddActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,
                        getString(R.string.toast_add_new_activity),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.editOption:
                intent = new Intent(MainActivity.this,
                        EditActivity.class);
                intent.putExtra("EDIT_ACTIVITY_NAME",
                        activity.getName());
                intent.putExtra("EDIT_ACTIVITY_DESCRIPTION",
                        activity.getDescription());
                intent.putExtra("PROJECT_NAME",
                        activity.getProjectParent().getName());
                startActivity(intent);
                Toast.makeText(MainActivity.this,
                        item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteOption:
                intent = new Intent(MainActivity.ERASE_ELEMENT);
                intent.putExtra("SWIPE_ACTIVITY_NAME",
                        activity.getName());
                intent.putExtra("PROJECT_NAME", activity.getName());
                sendBroadcast(intent);
                m_projectList.remove(m_lastVisibleItem);
                m_adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,
                        item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.reportOption:
                intent = new Intent(MainActivity.this,
                        ReportActivity.class);
                startActivity(intent);
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
        if (m_drawerMenu.isDrawerOpen(GravityCompat.START)){
            m_drawerMenu.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Log.i(TAG, "onBackPressed");
        Log.d(TAG, "Stop the service");
        sendBroadcast(new Intent(MainActivity.STOP_SERVICE));
        super.onBackPressed();
        finish();
    }
    /**
     * Flag that helps us choose the activity to present.
     */
    private boolean m_isCurrentParentRoot;
}
