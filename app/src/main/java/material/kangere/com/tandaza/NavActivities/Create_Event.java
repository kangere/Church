package material.kangere.com.tandaza.NavActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import material.kangere.com.tandaza.AppConfig;
import material.kangere.com.tandaza.JSONParser;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.StaticMethods;
import material.kangere.com.tandaza.videoimageupload.UploadActivity;

public class Create_Event extends AppCompatActivity {




    private static final String TAG_SUCCESS = "success";
    private static final String TAG = Create_Event.class.getSimpleName();

    private  String picturePath;
    private  String file_path;
    private String name,venue,description,ministries,date,time;
    private static final int PICK_FROM_GALLERY = 2;
    final int PIC_CROP = 2;

    //UI variables
    private Button upload,pick;
    private ImageView picView;
    private Spinner sministries;
    private EditText event_name,event_venue,event_description;
    static EditText displayDate,displayTime;
    private ProgressDialog progressDialog;

    RequestQueue requestQueue;
    JSONParser jsonParser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_create__event);

        StaticMethods.toolBar(this, R.id.tbCreateEvent);

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
        picView = (ImageView) findViewById(R.id.ivPoster);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                                            R.array.ministries,
                                                                            R.layout.myspinner);
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

        Snackbar progressBar;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = Snackbar.make(findViewById(android.R.id.content),"Uploading",Snackbar.LENGTH_LONG);
           if(progressDialog == null){
               progressDialog = Show_Notifications.createProgrssDialog(Create_Event.this);
               progressDialog.show();
           }else {
               progressDialog.show();
           }
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
            params.add(new BasicNameValuePair("posterpath",file_path));
            params.add(new BasicNameValuePair("description",description));

            JSONObject jsonObject = jsonParser.makeHttpRequest(AppConfig.EVENT_UPLOAD_URL,"POST",params);

            //check logcat
            Log.d("Create Response",jsonObject.toString());

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);

                if(success == 1){
                    //finish activity
                    finish();
                }else{
                    Log.e("Error","Json request failed");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Create_Event.this,"Something Seems to be wrong, Try again later",Toast.LENGTH_LONG).show();
                        }
                    });
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
            progressBar = Snackbar.make(findViewById(android.R.id.content),"Upload Successful",Snackbar.LENGTH_LONG);
            progressBar.dismiss();
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
    public void GetPoster(View view){
        try {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    PICK_FROM_GALLERY);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(Create_Event.this, "Phone does not support camera", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_FROM_GALLERY) {


                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                picView.setVisibility(View.VISIBLE);
                picView.setImageBitmap(thumbnail);
                //Log.d(TAG, picturePath);
                Toast.makeText(Create_Event.this, picturePath, Toast.LENGTH_LONG).show();

                launchUploadActivity();


            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                picView.setVisibility(View.VISIBLE);
                picView = (ImageView) findViewById(R.id.imgPreview);
//display the returned cropped image
                picView.setImageBitmap(thePic);
            } else if (requestCode == 2404) {

                //Retrives the image server file path from Upload Activity using the request code.
                file_path = data.getStringExtra("file_path");
                Toast.makeText(Create_Event.this, file_path, Toast.LENGTH_LONG).show();
                //Log.d(TAG,file_path);
            }
        }
    }
    private void launchUploadActivity() {
        //sends the android file path to the Upload Activity
        Intent i = new Intent(Create_Event.this, UploadActivity.class);
        i.putExtra("filePath", picturePath);

        startActivityForResult(i, 2404);

    }
}
