package core.ds.optionsmenu.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import core.ds.optionsmenu.Activities.EditActivity;
import core.ds.optionsmenu.Activities.MainActivity;
import core.ds.optionsmenu.Activities.ProjectActivity;
import core.ds.optionsmenu.Activities.TaskActivity;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CProject;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.Model.CTimeTrackerEngine;
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
    public void onBindViewHolder(final ListItemViewHolder viewHolder, final int position) {
        CProject model = (CProject) m_items.get(position);
        viewHolder.labelName.setText(model.getName());
        viewHolder.labelParentProject.setText(model
                .getProjectParent().getName());
        viewHolder.labelDescription.setText(model.getDescription());
        viewHolder.labelType.setText("Project");
        viewHolder.timeStart.setText(
                String.format("%s, %s",
                        CTimeTrackerEngine.getInstance()
                                .Day.format(new Date(model.getStartTime())),
                        CTimeTrackerEngine.getInstance()
                                .hour.format(new Date(model.getStartTime()))
                ));
        viewHolder.timeDuration.setText(
                String.format("%s",
                        CTimeTrackerEngine.getInstance()
                                .duration.format(new Date(model.getTotalTime()))
                ));
        viewHolder.timeEnd.setText(
                String.format("%s, %s",
                        CTimeTrackerEngine.getInstance()
                                .Day.format(new Date(model.getEndTime())),
                        CTimeTrackerEngine.getInstance()
                                .hour.format(new Date(model.getEndTime()))
                ));
        // Display or take away the tracking flag.
        if (model.isTracked()) {
            viewHolder.isTrackingButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.isTrackingButton.setVisibility(View.INVISIBLE);
        }
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
                                    final int pos,
                                    final long id) {
                Log.i(TAG, "onItemClick");
                Log.d(TAG, "pos = " + pos + ", id = " + id);

                // Get activity from the project's children
                CActivity activity = childList.get(pos);
                Intent intent = new Intent(
                        MainActivity.LOWER_LEVEL);
                intent.putExtra("CLICKED_ACTIVITY_NAME", activity.getName());
                intent.putExtra("PROJECT_NAME", model.getName());
                m_context.sendBroadcast(intent);
                if (activity instanceof CProject) {
                    // Present the project activity. In it the children will
                    // be requested.
                    m_context.startActivity(new Intent(m_context,
                            ProjectActivity.class));
                } else if (activity instanceof CTask) {
                    // Present the task activity. The intervals will be
                    // requested there.
                    m_context.startActivity(new Intent(m_context,
                            TaskActivity.class));
                } else {
                    assert false : "Activity is either project nor task.";
                }
                Toast.makeText(m_context,
                        "You clicked on " + activity.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.toolbar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return m_items.size();
    }

    /**
     * Helps in updating the list of elements of the adapter.
     * @param items
     */
    public void setItemList(final List items) {m_items = items;}
    /**
     * ViewHolder pattern to smooth scroll. */
    public final static class ListItemViewHolder
            extends RecyclerView.ViewHolder {
        // Variables of the UI
        public TextView labelName;
        public TextView labelParentProject;
        public TextView labelType;
        public ImageButton isTrackingButton;
        public TextView labelDescription;
        public TextView timeStart;
        public TextView timeDuration;
        public TextView timeEnd;
        public NonScrollListView childrenList;
        public Toolbar toolbar;
        /**
         * Constructor for item holder. It retrieves the UI elements from the
         * layout for later use.
         * @param itemView The view that is going to be rendered. */
        public ListItemViewHolder(final View itemView) {
            super(itemView);
            labelName = itemView.findViewById(R.id.labelName);
            labelParentProject = itemView.findViewById(R.id.labelParentProject);
            labelDescription = itemView.findViewById(R.id.labelDescription);
            labelType = itemView.findViewById(R.id.labelType);
            timeStart = itemView.findViewById(R.id.timeStart);
            timeDuration = itemView.findViewById(R.id.timeDuration);
            timeEnd = itemView.findViewById(R.id.timeEnd);
            isTrackingButton = itemView.findViewById(R.id.isTrackingButton);
            childrenList = itemView.findViewById(R.id.listView);
            toolbar = itemView.findViewById(R.id.toolbar);
        }
    }

}
