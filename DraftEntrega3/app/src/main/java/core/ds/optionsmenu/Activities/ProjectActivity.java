package core.ds.optionsmenu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

import core.ds.optionsmenu.Adapters.CActivityListAdapter;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CProject;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.R;

public class ProjectActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProjectActivity";

    // Variables of the UI
    private TextView labelName;
    private TextView labelDescription;
    private TextView labelStart;
    private TextView labelDuration;
    private TextView labelEnd;
    private ListView m_activityListView;
    private CActivityListAdapter m_activityListAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cactivity_view_layout);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        CActivity activity = (CActivity) intent.getSerializableExtra(MainActivity.TIMETRACKER_PROJECT_NAME);
        CProject project = (CProject) activity;

        // Get the view element from the UI through its id
        labelName = findViewById(R.id.labelName);
        labelDescription = findViewById(R.id.labelDescription);
        labelStart = findViewById(R.id.timeStart);
        labelDuration = findViewById(R.id.timeDuration);
        labelEnd = findViewById(R.id.timeEnd);

        // Set the text to the corresponding task information
        labelName.setText(project.getName());
        labelDescription.setText(project.getDescription());
        labelStart.setText(new Date(project.getStartTime()).toString());
        labelDuration.setText(new Date(project.getTotalTime()).toString());
        labelEnd.setText(new Date(project.getEndTime()).toString());

        // Array of high level projects of the application
        ArrayList<CActivity> activityList =
                new ArrayList<>(project.getChildren());

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
                Intent intent;
                if (activity instanceof CTask) {
                    intent = new Intent(ProjectActivity.this, TaskActivity.class);
                    intent.putExtra(MainActivity.TIMETRACKER_TASK_NAME, activity);
                } else {
                    intent = new Intent(ProjectActivity.this, ProjectActivity.class);
                    intent.putExtra(MainActivity.TIMETRACKER_PROJECT_NAME, activity);
                }
                startActivity(intent); // Start the activity
                Toast.makeText(ProjectActivity.this,
                        "You clicked on " + activity.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.backButton:
                Toast.makeText(ProjectActivity.this, "Back", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

}
