package core.ds.optionsmenu.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import core.ds.optionsmenu.Activities.MainActivity;
import core.ds.optionsmenu.Activities.ProjectActivity;
import core.ds.optionsmenu.Activities.TaskActivity;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CProject;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.View.NonScrollListView;
import core.ds.optionsmenu.R;

public class CActivityHorizontalRecyclerAdapter
        extends RecyclerView.Adapter<CActivityHorizontalRecyclerAdapter.ListItemViewHolder> {

    // Tag for something i don't know
    private static final String TAG = "CActivityListAdapter";
    private Context m_context; // TODO: What is context?
    private List<CActivity> m_items; // List of activities displayed on screen

    /**
     * Recycler Adapter constructor to initialize context (used on onBindViewHolder)
     * and objects which carries the data to display.
     * @param objects The list of objects.
     * @param context The context of the view. */
    public CActivityHorizontalRecyclerAdapter(final List<CActivity> objects, final Context context) {
        m_items = objects;
        m_context = context;
    }

    /**
     * Triggered when a new item is to be created. It returns an inflated view
     * holder with the UI components initialized.
     * @param parent The ViewGroup to which it belongs.
     * @param viewType The view type id. */
    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(final ViewGroup parent,
                                                 final int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.cactivity_view_layout,
                        parent, false);
        return new ListItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, final int position) {
        CProject model = (CProject) m_items.get(position);

        viewHolder.labelName.setText(model.getName());
        viewHolder.labelDescription.setText(model.getDescription());

        // Array of objects to display in list
        ArrayList<CActivity> childList = new ArrayList(model.getChildren());
        // Project list adapter used to display on screen
        CActivityListAdapter childrenListAdapter = new CActivityListAdapter(
                m_context,
                R.layout.cactivity_item_view_layout,
                childList);
        viewHolder.childrenList.setAdapter(childrenListAdapter);

        viewHolder.childrenList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(final AdapterView<?> parent,
                                    final View view,
                                    final int position,
                                    final long id) {
                // Get activity from the project's children
                CActivity activity = childList.get(position);
                // Create the intent for the corresponding activity
                Intent intent;
                if (activity instanceof CTask) {
                    intent = new Intent(m_context, TaskActivity.class);
                    intent.putExtra(MainActivity.TIMETRACKER_TASK_NAME, activity);
                } else {
                    intent = new Intent(m_context, ProjectActivity.class);
                    intent.putExtra(MainActivity.TIMETRACKER_PROJECT_NAME, activity);
                }
                m_context.startActivity(intent); // Start the activity
                Toast.makeText(m_context,
                        "You clicked on " + activity.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_items.size();
    }

    /**
     * ViewHolder pattern to smooth scroll. */
    public final static class ListItemViewHolder
            extends RecyclerView.ViewHolder {
        // Variables of the UI
        public TextView labelName;
        public TextView labelDescription;
        public TextView labelStart;
        public TextView labelDuration;
        public TextView labelEnd;
        public NonScrollListView childrenList;

        /**
         * Constructor for item holder. It retrieves the UI elements from the
         * layout for later use.
         * @param itemView The view that is going to be rendered. */
        public ListItemViewHolder(final View itemView) {
            super(itemView);
            labelName = itemView.findViewById(R.id.labelName);
            labelDescription = itemView.findViewById(R.id.labelDescription);
            labelStart = itemView.findViewById(R.id.labelStart);
            labelDuration = itemView.findViewById(R.id.labelDuration);
            labelEnd = itemView.findViewById(R.id.labelEnd);
            childrenList = itemView.findViewById(R.id.listView);
        }
    }

}
