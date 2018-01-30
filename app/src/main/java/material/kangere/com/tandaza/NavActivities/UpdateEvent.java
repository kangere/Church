package material.kangere.com.tandaza.NavActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.ApiFields;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.RequestQueueSingleton;

/**
 * Created by user on 20/12/2017.
 */

public class UpdateEvent extends AppCompatActivity {

    private static TextView date,time;
    private String id_update;
    private final String TAG = UpdateEvent.class.getSimpleName();
    private EditText name, description, location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_event);
        setTitle("Update Event");

        Toolbar toolbar = findViewById(R.id.tbUpdateEvent);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));

        //enable up navigation
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);



        Button updateDate, updateTime, UpdateEvent;

        Bundle bundle = getIntent().getExtras();
        String[] single_event = bundle.getStringArray("single_event");

        name = findViewById(R.id.etUpdateEventName);
        description = findViewById(R.id.etUpdateEventDesc);
        location = findViewById(R.id.etUpdateEventLocation);

        date = findViewById(R.id.tvUpdateEventDate);
        time = findViewById(R.id.tvUpdateEventTime);

        updateDate = findViewById(R.id.bUpdateEventDate);
        updateTime = findViewById(R.id.bUpdateEventTime);
        UpdateEvent = findViewById(R.id.bUpdateEvent);

        if(single_event != null)
        {
            Log.d("Date" ,single_event[2]);
            Date date1 = Date.valueOf(single_event[2]);
            String formatted_date = DateFormat.getDateInstance().format(date1);

            name.setText(single_event[0]);
            date.setText(formatted_date);
            time.setText(single_event[3]);
//            Ministry.setText(single_event[4]);
            location.setText(single_event[5]);
            description.setText(single_event[6]);
            id_update = single_event[7];
        }

        updateDate.setOnClickListener(
                view -> {

                    DialogFragment dialogFragment = new DatePickerFragment();
                    dialogFragment.show(getFragmentManager(), "datePicker");
                }
        );

        updateTime.setOnClickListener(
                view -> {
                    DialogFragment dialogFragment = new TimePickerFragment();
                    dialogFragment.show(getFragmentManager(), "timePicker");
                }
        );

        UpdateEvent.setOnClickListener(
                view -> Update()


        );
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.setText(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
        }
    }

    private void Update()
    {

        String title = name.getText().toString();
        String venue = location.getText().toString();
        String desc = description.getText().toString();
        String uDate = date.getText().toString();
        String uTime = time.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_EVENT,
                response -> {

                    Log.d(TAG, response);

                    int success = 0;
                    try {
                        JSONObject object = new JSONObject(response);
                        success = object.getInt("success");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    //check if result is successful
                    if(success == 1){

                        Toast.makeText(this,"Event updated successfully",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(this,"Unable to update Event",Toast.LENGTH_LONG).show();
                    }

                },
                error -> Log.e(TAG, error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put(ApiFields.TAG_EVENT_ID,id_update);
                params.put(ApiFields.TAG_TITLE,title);
                params.put(ApiFields.TAG_VENUE,venue);
                params.put(ApiFields.TAG_DESCRIPTION,desc);
                params.put(ApiFields.TAG_DATE,uDate);
                params.put(ApiFields.TAG_TIME,uTime);



                return params;
            }
        };

        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
