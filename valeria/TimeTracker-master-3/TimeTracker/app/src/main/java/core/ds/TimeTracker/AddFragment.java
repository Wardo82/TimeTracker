package core.ds.TimeTracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AddFragment extends Fragment {

    private EditText editName;
    private EditText editDescription;
    private Button buttonAddProject;
    private Button buttonAddTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add, container,false);

        editName = (EditText) view.findViewById(R.id.activity_name);
        editDescription = (EditText) view.findViewById(R.id.activity_description);
        buttonAddProject = (Button) view.findViewById(R.id.project_add_button);
        buttonAddTask = (Button) view.findViewById(R.id.task_add_button);

        buttonAddProject.setOnClickListener(onClickListener);
        buttonAddTask.setOnClickListener(onClickListener);

        return view;
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            String name;
            String description;
            name = editName.getText().toString();
            description = editDescription.getText().toString();
            CTimeTrackerEngine TimeTracker = CTimeTrackerEngine.getInstance(); // First and only instance of CTimeTrackerEngine
            CProject P1 = (CProject) TimeTracker.getActivity("P1"); // Get a reference to P1
            switch(v.getId()){

                case R.id.project_add_button:

                    P1.appendActivity(new CProject(name,description) ); // Create P1
                    break;
                case R.id.task_add_button:
                    System.out.println("In task.");
                    P1.appendActivity(new CTask(name,description));
            }
        }
    };
}
