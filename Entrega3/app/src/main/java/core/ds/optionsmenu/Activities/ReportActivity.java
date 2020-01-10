package core.ds.optionsmenu.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import core.ds.optionsmenu.Model.CClock;
import core.ds.optionsmenu.Model.CTimeTrackerEngine;
import core.ds.optionsmenu.Model.CVisitorFormatter;
import core.ds.optionsmenu.Model.CVisitorFormatterHtml;
import core.ds.optionsmenu.Model.CVisitorFormatterText;
import core.ds.optionsmenu.Model.CVisitorReporter;
import core.ds.optionsmenu.Model.CVisitorReporterBrief;
import core.ds.optionsmenu.Model.CVisitorReporterDetailed;
import core.ds.optionsmenu.R;

public class ReportActivity extends AppCompatActivity
        implements View.OnClickListener{
    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();

    //UI Components
    private Button m_briefHTML;
    private Button m_briefText;
    private Button m_detailedHTML;
    private Button m_detailedText;
    private ImageButton m_datePickerFrom;
    private ImageButton m_timePickerFrom;
    private TextView m_labelDateFrom;
    private TextView m_labelTimeFrom;
    private ImageButton m_datePickerUntil;
    private ImageButton m_timePickerUntil;
    private TextView m_labelDateUntil;
    private TextView m_labelTimeUntil;

    /**
     * On create method called when this activity is presented.
     */
    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_view_layout);

        // Get the ui components from the activity
        m_briefHTML = findViewById(R.id.briefHTML);
        m_briefText = findViewById(R.id.briefText);
        m_detailedHTML = findViewById(R.id.detailedHTML);
        m_detailedText = findViewById(R.id.detailedText);

        // Information for "From"
        m_datePickerFrom = findViewById(R.id.datePickerFrom);
        m_timePickerFrom = findViewById(R.id.timePickerFrom);
        m_labelDateFrom = findViewById(R.id.dateFrom);
        m_labelTimeFrom = findViewById(R.id.dateFromTime);

        // Information for "Until"
        m_datePickerUntil = findViewById(R.id.datePickerUntil);
        m_timePickerUntil = findViewById(R.id.timePickerUntil);
        m_labelDateUntil = findViewById(R.id.dateUntil);
        m_labelTimeUntil = findViewById(R.id.dateUntilTime);

        // Handle report button's click
        m_briefText.setOnClickListener(this);
        m_briefHTML.setOnClickListener(this);
        m_detailedText.setOnClickListener(this);
        m_detailedHTML.setOnClickListener(this);

        // Handle date and time picker0s click
        m_datePickerFrom.setOnClickListener(this);
        m_timePickerFrom.setOnClickListener(this);
        m_datePickerUntil.setOnClickListener(this);
        m_timePickerUntil.setOnClickListener(this);
    }

    /**
     * Implements the onClick event listener and handles the click of a UI
     * element on the screen based on the clicked element's id
     * @param v
     */
    @Override
    public void onClick(View v) {
        // Get Current Date
        DatePickerDialog datePickerDialog;
        TimePickerDialog timePickerDialog;
        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay, mHour, mMinute;

        switch (v.getId()) {
            // Implement the listener of each button and send the
            // corresponding visitor.
            case R.id.briefHTML:
            case R.id.briefText:
            case R.id.detailedHTML:
            case R.id.detailedText:
                handleReport(v.getId());
                break;
            case R.id.datePickerFrom:
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                m_labelDateFrom.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = formatter.parse(m_labelDateUntil.getText().toString());
                    datePickerDialog.getDatePicker().setMaxDate(date.getTime());
                } catch (Exception e) {}

                datePickerDialog.show();
                break;
            case R.id.timePickerFrom:
                // Get Current Time
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                m_labelTimeFrom.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();

                break;
            case R.id.datePickerUntil:
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                m_labelDateUntil.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = formatter.parse(m_labelDateFrom.getText().toString());
                    datePickerDialog.getDatePicker().setMinDate(date.getTime());
                } catch (Exception e) {}

                datePickerDialog.show();

                break;
            case R.id.timePickerUntil:
                // Get Current Time
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                m_labelTimeUntil.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
                break;
        }
    }

    /**
     * Hanldes the event in case it is a report. It creates the necessary
     * visitors depending on the report ID.
     * @param reportId The report id.
     */
    private void handleReport(final int reportId) {
        CVisitorFormatter formatter;
        CVisitorReporter reporter;

        long start;
        long end;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm");
            start = sdf.parse(m_labelDateFrom.getText().toString()
                    + " " + m_labelTimeFrom.getText().toString())
                    .getTime();
            end = sdf.parse(m_labelDateUntil.getText().toString()
                    + " " + m_labelTimeUntil.getText().toString())
                    .getTime();

        } catch (Exception e) {
            start = 0;
            end = CClock.getInstance().getTime();
            Toast.makeText(this, "Generating report from the begining.",
                    Toast.LENGTH_LONG).show();
        }

        switch (reportId) {
            case R.id.briefHTML:
                formatter = new CVisitorFormatterHtml(start, end);
                reporter =
                        new CVisitorReporterBrief(formatter);
                CTimeTrackerEngine.getInstance().generateReport(reporter);
                break;
            case R.id.briefText:
                formatter = new CVisitorFormatterText(start, end);
                reporter =
                        new CVisitorReporterBrief(formatter);
                CTimeTrackerEngine.getInstance().generateReport(reporter);
                break;
            case R.id.detailedHTML:
                formatter = new CVisitorFormatterHtml(start, end);
                reporter =
                        new CVisitorReporterDetailed(formatter);
                CTimeTrackerEngine.getInstance().generateReport(reporter);
                break;
            case R.id.detailedText:
                formatter = new CVisitorFormatterText(start, end);
                reporter =
                        new CVisitorReporterDetailed(formatter);
                CTimeTrackerEngine.getInstance().generateReport(reporter);
                break;
        }

        finish();
    }

    /**
     * Hanldes the event in case it is a picker that was handled.
     * It assigns the dates and time to the corresponding labels.
     * @param reportId The report id.
     */
    private void handleDate(final int reportID) {

    }
}
