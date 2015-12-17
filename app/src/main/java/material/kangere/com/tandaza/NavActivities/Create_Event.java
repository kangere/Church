package material.kangere.com.tandaza.NavActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import material.kangere.com.tandaza.AppConfig;
import material.kangere.com.tandaza.InitToolbar;
import material.kangere.com.tandaza.JSONParser;
import material.kangere.com.tandaza.R;

public class Create_Event extends AppCompatActivity {



    private ProgressDialog progressDialog;
    private static final String TAG_SUCCESS = "success";
    static EditText displayDate,displayTime;
    private Spinner sministries;
    private Button upload;
    private EditText event_name,event_venue,event_description;
    private String name,venue,description,ministries,date,time;
    RequestQueue requestQueue;
    JSONParser jsonParser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_create__event);

        InitToolbar.toolBar(this,R.id.tbCreateEvent);

        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

       displayDate = (EditText)findViewById(R.id.etSHowDate);
        displayTime = (EditText)findViewById(R.id.etSHowTime);
        sministries = (Spinner) findViewById(R.id.sEventMinistries);
        upload = (Button) findViewById(R.id.bEventUpload);
        event_name = (EditText) findViewById(R.id.etEventName);
        event_description = (EditText) findViewById(R.id.etEventDescription);
        event_venue = (EditText) findViewById(R.id.etEventVenue);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                                            R.array.ministries,
                                                                            android.R.layout.simple_spinner_item);
        sministries.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        jsonParser = new JSONParser();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }


        return super.onOptionsItemSelected(item);
    }

    public  static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new android.app.DatePickerDialog(getActivity(),this,year,month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
               displayDate.setText(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
        }
    }
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,hour,minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            displayTime.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
        }
    }

    public void SetDate(View view){
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(),"datePicker");
    }
    public void SetTime(View view){
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.show(getFragmentManager(),"timePicker");
    }

    public void UploadEvent(View view){
        new UploadEvent().execute();
    }

    private class UploadEvent extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Create_Event.this);
            progressDialog.setMessage("Uploading");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {


            getText();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("event_name",name));
            params.add(new BasicNameValuePair("event_date",date));
            params.add(new BasicNameValuePair("event_time",time));
            params.add(new BasicNameValuePair("ministry",ministries));
            params.add(new BasicNameValuePair("venue",venue));
            params.add(new BasicNameValuePair("description",description));

            JSONObject jsonObject = jsonParser.makeHttpRequest(AppConfig.EVENT_UPLOAD_URL,"POST",params);

            //check logcat
            Log.d("Create Respons",jsonObject.toString());

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);

                if(success == 1){
                    //finish activity
                    finish();
                }else{
                    Log.d("Error","Json request failed");
                }

            }catch(JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    private void getText(){
        name = event_name.getText().toString();
        venue = event_venue.getText().toString();
        description = event_description.getText().toString();
        ministries = sministries.getSelectedItem().toString();
        date = displayDate.getText().toString();
        time = displayTime.getText().toString();
    }

}
