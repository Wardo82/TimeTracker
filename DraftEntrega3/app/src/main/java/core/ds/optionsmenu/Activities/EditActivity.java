package core.ds.optionsmenu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import core.ds.optionsmenu.R;

public class EditActivity extends AppCompatActivity {
    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();

    public Button m_saveButton;
    public TextView m_nameText;
    public TextView m_descriptionText;

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_edit_layout);

        Intent intent = getIntent();
        String editActivityName = intent.getStringExtra(
                "EDIT_ACTIVITY_NAME");
        String editActivityDescription = intent.getStringExtra(
                "EDIT_ACTIVITY_DESCRIPTION");
        String projectParentName = intent.getStringExtra(
                "PROJECT_NAME");
        // Get the activity to be a tiny floating square
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8),
                (int) (height * .6));

        m_saveButton = (Button) findViewById(R.id.saveButton);
        m_nameText = (TextView) findViewById(R.id.editName);
        m_descriptionText = (TextView) findViewById(R.id.editDescription);
        m_nameText.setText(editActivityName);
        m_descriptionText.setText(editActivityDescription);

        // Add the click listener to the button
        m_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!m_nameText.getText().toString().isEmpty()
                    && !m_descriptionText.getText().toString().isEmpty()) {
                    Log.d(TAG,
                            "Editing name: "
                            + m_nameText.getText().toString()
                            + " and description: "
                            + m_descriptionText.getText().toString());
                    // Create the intent to send edited data
                    Intent intent = new Intent(EditActivity.EDIT_ELEMENT);
                    intent.putExtra("EDIT_ACTIVITY_NAME", editActivityName);
                    intent.putExtra("PROJECT_NAME", projectParentName);
                    intent.putExtra("NEW_NAME",
                            m_nameText.getText().toString());
                    intent.putExtra("NEW_DESCRIPTION",
                            m_descriptionText.getText().toString());
                    sendBroadcast(intent);
                    finish();
                } else {
                    Toast.makeText(EditActivity.this,
                            "Please fill the text boxes",
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
    public static String EDIT_ELEMENT = "Edit_element";
}
