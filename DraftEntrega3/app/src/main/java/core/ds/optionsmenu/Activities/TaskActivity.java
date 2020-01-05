package core.ds.optionsmenu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

import core.ds.optionsmenu.Adapters.CIntervalListAdapter;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CInterval;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.R;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TaskActivity";

    // Variables of the UI
    private TextView labelName;
    private TextView labelDescription;
    private TextView labelStart;
    private TextView labelDuration;
    private TextView labelEnd;
    private ListView m_intervalListView;
    private CIntervalListAdapter m_intervalListAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cactivity_view_layout);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        CActivity activity = (CActivity) intent.getSerializableExtra(MainActivity.TIMETRACKER_TASK_NAME);
        CTask task = (CTask) activity;

        // Get the view element from the UI through its id
        labelName = findViewById(R.id.labelName);
        labelDescription = findViewById(R.id.labelDescription);
        labelStart = findViewById(R.id.timeStart);
        labelDuration = findViewById(R.id.timeDuration);
        labelEnd = findViewById(R.id.timeEnd);

        // Set the text to the corresponding task information
        labelName.setText(task.getName());
        labelDescription.setText(task.getDescription());
        labelStart.setText(new Date(task.getStartTime()).toString());
        labelDuration.setText(new Date(task.getTotalTime()).toString());
        labelEnd.setText(new Date(task.getEndTime()).toString());

        // Array of high level projects of the application
        ArrayList<CInterval> intervalList =
                new ArrayList<>(task.getChildren());

        // Get the main horizontal recycler view, adapter and layout manager.
        m_intervalListView = findViewById(R.id.listView);
        m_intervalListAdapter = new CIntervalListAdapter(
                this,
                R.layout.cinterval_item_view_layout,
                intervalList);

        // Apply the adapter to the list view.
        m_intervalListView.setAdapter(m_intervalListAdapter);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.backButton:
                Toast.makeText(TaskActivity.this, "Back", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
