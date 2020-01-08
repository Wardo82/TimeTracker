package core.ds.optionsmenu.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import core.ds.optionsmenu.Activities.ProjectActivity;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CProject;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.Model.CTimeTrackerEngine;
import core.ds.optionsmenu.R;

public class CActivityListAdapter extends ArrayAdapter<CActivity> {

    // Tag for something i don't know
    private static final String TAG = "CActivityListAdapter";

    // Context for something i don't know
    private Context m_context;
    private int m_resourceId;
    private List<CActivity> m_childrenList;
    /**
     * Class constructor for the list that displays the activities contained
     * in a project.
     * @param context Context where the list is.
     * @param resource The identifier.
     * @param objects The list of activities. */
    public CActivityListAdapter(final Context context,
                                final int resource,
                                final ArrayList<CActivity> objects) {
        super(context, resource, objects);
        m_context = context;
        m_resourceId = resource;
        m_childrenList = objects;
    }

    /**
     * ViewHolder pattern to smooth scroll. */
    static class ViewHolder {
        public TextView name;
        public Button runButton;
        public boolean isTracked = false;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, final @NonNull ViewGroup parent) {
        // Variables for the cell
        CActivity model = getItem(position);
        String name = model.getName();

        ViewHolder holder; // Holder variable for ViewHolder pattern
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(m_context);
            convertView = inflater.inflate(m_resourceId, parent, false);

            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.textView);
            holder.runButton = convertView.findViewById(R.id.button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Take the button off if it is a Project. Projects can't be tracked.
        if (getItem(position) instanceof CProject) {
            holder.runButton.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(Color.BLACK);
        } else {
            holder.runButton.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(ContextCompat
                    .getColor(m_context,
                            R.color.backcolor));
        }
        // Change label according to tracking condition
        if ((CTimeTrackerEngine.getInstance().m_trackedTask
                .containsKey(name))) {
            holder.runButton.setText("PAUSE");
            convertView.setBackgroundColor(ContextCompat
                    .getColor(m_context,
                            R.color.orange));
        } else {
            holder.runButton.setText("PLAY");
        }
        holder.name.setText(name); // Set the name of the item
        holder.runButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i(TAG, "onButtonClick");
                Log.d(TAG, "Clicked view" + v.getTag());
                switch (v.getId()) {
                    case R.id.button:
                        Intent intent;
                        if (!(CTimeTrackerEngine.getInstance().m_trackedTask
                                .containsKey(name))) {
                            holder.runButton.setText("PAUSE");
                            intent = new Intent(
                                    ProjectActivity.START_CHRONO);
                            Toast.makeText(m_context,
                                    "Start tracking " + name,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            holder.runButton.setText("PLAY");
                            intent = new Intent(
                                        ProjectActivity.STOP_CHRONO);
                            Toast.makeText(m_context,
                                    "Stop tracking " + name,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // The name of the clicked item
                        intent.putExtra("CLICKED_TASK",
                                name);
                        intent.putExtra("PARENT_NAME",
                                model.getProjectParent().getName());
                        m_context.sendBroadcast(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        return convertView;
    }

    public void remove(final int position) {
        m_childrenList.remove(position);
        notifyDataSetChanged();
    }
}
