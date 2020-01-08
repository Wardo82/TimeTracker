package core.ds.optionsmenu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.R;

public class AddActivity extends AppCompatActivity {
    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();

    public Button m_saveButton;
    public TextView m_nameText;
    public TextView m_descriptionText;
    private TextView m_forText;
    private TextView m_inText;
    public RadioGroup m_activityRadioGroup;

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_layout);

        // Get the activity to be a tiny floating square
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8),
                (int) (height * .8));

        m_nameText = (TextView) findViewById(R.id.editName);
        m_descriptionText = (TextView) findViewById(R.id.editDescription);
        m_forText = findViewById(R.id.editFor);
        m_inText = findViewById(R.id.editIn);
        m_saveButton = (Button) findViewById(R.id.saveButton);
        m_activityRadioGroup = findViewById(R.id.activityRadioGroup);
        findViewById(R.id.taskSpecs)
                .setVisibility(View.INVISIBLE);
        m_activityRadioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(final RadioGroup group,
                                         final int checkedId) {
                // This will get the radiobutton that has changed in
                // its check state.
                RadioButton checkedRadioButton =
                        (RadioButton) group.findViewById(checkedId);
                if (checkedId == R.id.taskRadio) {
                    findViewById(R.id.taskSpecs)
                            .setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.taskSpecs)
                            .setVisibility(View.INVISIBLE);
                }

                Log.d(TAG, checkedRadioButton.getText().toString());
            }
        });

        m_nameText.setText(getString(R.string.placeholder_name));
        m_descriptionText.setText(getString(R.string.placeholder_description));
        // Add the click listener to the button
        m_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!m_nameText.getText().toString().isEmpty()
                        && !m_descriptionText.getText().toString().isEmpty()) {
                    Log.d(TAG,
                            "Adding name: "
                                    + m_nameText.getText().toString()
                                    + " and description: "
                                    + m_descriptionText.getText().toString());
                    // Create the intent to send edited data
                    Intent intent = new Intent(AddActivity.ADD_ELEMENT);
                    intent.putExtra("ACTIVITY_NAME",
                            m_nameText.getText().toString());
                    intent.putExtra("ACTIVITY_DESCRIPTION",
                            m_descriptionText.getText().toString());
                    // Check for which option is selected
                    int checkedId = m_activityRadioGroup
                            .getCheckedRadioButtonId();
                    if (checkedId == R.id.taskRadio) {
                        intent.putExtra("IS_TASK", true);
                        intent.putExtra("TASK_PROGRAMMED",
                                Long.valueOf(m_forText.getText().toString())
                                        * 60 * 1000);
                        intent.putExtra("TASK_LIMITED",
                                Long.valueOf(m_inText.getText().toString())
                                        * 60 * 1000);
                    }
                    sendBroadcast(intent);
                    finish();
                } else {
                    Toast.makeText(AddActivity.this,
                            getString(R.string.toast_please_fill_boxes),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * String that defines the action of asking the <code>ProjectActivity
     * </code> for the name and description and sending back the edited
     * information.
     */
    public static String ADD_ELEMENT = "Add_element";
}
