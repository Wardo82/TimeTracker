package core.ds.optionsmenu.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

import core.ds.optionsmenu.Model.CInterval;
import core.ds.optionsmenu.R;

public class CIntervalListAdapter extends ArrayAdapter<CInterval> {

    // Tag for something i don't know
    private static final String TAG = "CActivityListAdapter";
    // Context for something i don't know
    private Context m_context;
    private int m_resourceId;

    /**
     * Default constructor for the Interval list displayed in TaskActivity
     * @param context The context of the activity (Should be TaskActivity).
     * @param resource The id identifying the resource.
     * @param objects The array of intervals to be displayed. */
    public CIntervalListAdapter(final Context context,
                                final int resource,
                                final ArrayList<CInterval> objects) {
        super(context, resource, objects);
        m_context = context;
        m_resourceId = resource;
    }

    /**
     * ViewHolder pattern to smooth scroll. */
    static class ViewHolder {
        public TextView labelStart;
        public TextView labelDuration;
        public TextView labelEnd;
    }

    /**
     * Override of getView for the CIntervalListAdapter class necessary
     * to return the view item. */
    @NonNull
    @Override
    public View getView(final int position,
                        @Nullable View convertView,
                        final @NonNull ViewGroup parent) {

        // Get the information of the interval item
        String start = new Date(getItem(position).getStartTime()).toString();
        String duration = new Date(getItem(position).getTotalTime()).toString();
        String end = new Date(getItem(position).getTotalTime()).toString();

        ViewHolder holder; // The item holder
        if (convertView == null) {
            // Call the inflater object to inflate the view
            LayoutInflater inflater = LayoutInflater.from(m_context);
            convertView = inflater.inflate(m_resourceId, parent, false);

            holder = new CIntervalListAdapter.ViewHolder();
            holder.labelStart = convertView.findViewById(R.id.labelStart);
            holder.labelDuration = convertView.findViewById(R.id.labelDuration);
            holder.labelEnd = convertView.findViewById(R.id.labelEnd);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the texts corresponding to the interval times
        holder.labelStart.setText(start);
        holder.labelDuration.setText(duration);
        holder.labelEnd.setText(end);

        return convertView;
    }
}
