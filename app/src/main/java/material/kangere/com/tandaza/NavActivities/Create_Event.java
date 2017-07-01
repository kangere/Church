package material.kangere.com.tandaza.NavActivities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

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
import material.kangere.com.tandaza.videoimageupload.UploadActivity;

public class Create_Event extends Fragment implements View.OnClickListener{


    private static final String TAG_SUCCESS = "success";
    private static final String TAG = Create_Event.class.getSimpleName();

    private String picturePath;
    private String file_path;
    private String name, venue, description, ministries, date, time;
    private static final int PICK_FROM_GALLERY = 2;
    final int PIC_CROP = 2;

    //UI variables
    private ImageView picView;
    private Spinner sministries;
    private EditText event_name, event_venue, event_description;
    AutoCompleteTextView autoCompleteTextView;
    static EditText displayDate, displayTime;
    private ProgressDialog progressDialog;
    private String placeVenue;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    JSONParser jsonParser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        } catch (NullPointerException e) {
            Log.d(TAG, " " + e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_create__event, container, false);

        displayDate = (EditText) view.findViewById(R.id.etSHowDate);
        displayTime = (EditText) view.findViewById(R.id.etSHowTime);
        sministries = (Spinner) view.findViewById(R.id.sEventMinistries);
        event_name = (EditText) view.findViewById(R.id.etEventName);
        event_description = (EditText) view.findViewById(R.id.etEventDescription);
        event_venue = (EditText) view.findViewById(R.id.etEventVenue);
        picView = (ImageView) view.findViewById(R.id.ivPoster);
        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.tvAutoVenue);

        //init button
        Button date = (Button) view.findViewById(R.id.bDate);
        Button time = (Button) view.findViewById(R.id.bTime);
        Button poster = (Button) view.findViewById(R.id.bEventPoster);
        Button upload = (Button) view.findViewById(R.id.bEventUpload);
        //set clickListeners
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        poster.setOnClickListener(this);
        upload.setOnClickListener(this);

        //placeAutocompleteFragment.setOnPlaceSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.ministries,
                R.layout.myspinner);
        sministries.setAdapter(adapter);

        //requestQueue = Volley.newRequestQueue(getActivity());
        jsonParser = new JSONParser();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getFragmentManager().popBackStack();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bTime:
                SetTime();
                break;
            case R.id.bDate:
                SetDate();
                break;
            case R.id.bEventPoster:
                verifyStoragePermissions(getActivity());
                GetPoster();
                break;
            case R.id.bEventUpload:

                if(fieldsAreEmpty())
                   Toast.makeText(getActivity(),"One of The Fields is Empty", Toast.LENGTH_LONG).show();
                else UploadEvent();
                break;

        }
    }

    /**
     * Method checks if one or more fields are empty
     * @return true if none of the fields are empty otherwise returns false
     */
    private boolean fieldsAreEmpty() {
        if (event_name.getText().toString().isEmpty() ||
                event_venue.getText().toString().isEmpty() ||
                    event_description.getText().toString().isEmpty() ||
                        sministries.getSelectedItem().toString().isEmpty() ||
                            displayDate.getText().toString().isEmpty() ||
                                displayTime.getText().toString().isEmpty())
            return true;

        return false;
    }



    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            //DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //dialog.getDatePicker().setMinDate(new Date(Calendar.getInstance().).getTime());

            return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            displayDate.setText(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
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
            displayTime.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
        }
    }

    private void SetDate() {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    private void SetTime() {
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.show(getFragmentManager(), "timePicker");
    }

    private void UploadEvent() {
        new UploadEvent().execute();
    }

    private class UploadEvent extends AsyncTask<Void, Void, Void> {

        Snackbar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Uploading", Snackbar.LENGTH_LONG);
            if (progressDialog == null) {
                progressDialog = Show_Notifications.createProgrssDialog(getActivity());
                progressDialog.show();
            } else {
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {


            getText();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("event_name", name));
            params.add(new BasicNameValuePair("event_date", date));
            params.add(new BasicNameValuePair("event_time", time));
            params.add(new BasicNameValuePair("ministry", ministries));
            params.add(new BasicNameValuePair("venue", venue));
            params.add(new BasicNameValuePair("posterpath", file_path));
            params.add(new BasicNameValuePair("description", description));

            JSONObject jsonObject = jsonParser.makeHttpRequest(AppConfig.EVENT_UPLOAD_URL, "POST", params);

            //check logcat
            Log.d("Create Response", jsonObject.toString());

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);

                if (success == 1) {
                    //finish activity
                    // finish();
                    //go to previous fragment
                    UpcomingEvents upcomingEvents = new UpcomingEvents();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.flContent, upcomingEvents);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                } else {
                    Log.e("Error", "Json request failed");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Something Seems to be wrong, Try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            progressBar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Upload Successful", Snackbar.LENGTH_LONG);
            progressBar.dismiss();
        }
    }

    private void getText() {
        name = event_name.getText().toString();
        venue = event_venue.getText().toString();
        description = event_description.getText().toString();
        ministries = sministries.getSelectedItem().toString();
        date = displayDate.getText().toString();
        time = displayTime.getText().toString();
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void GetPoster() {
        try {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    PICK_FROM_GALLERY);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getActivity(), "Phone does not support camera", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {

            if (requestCode == PICK_FROM_GALLERY) {


                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                picView.setVisibility(View.VISIBLE);
                picView.setImageBitmap(thumbnail);
                //Log.d(TAG, picturePath);
                Toast.makeText(getActivity(), picturePath, Toast.LENGTH_LONG).show();

                launchUploadActivity();


            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                picView.setVisibility(View.VISIBLE);
                picView = (ImageView) getActivity().findViewById(R.id.imgPreview);
//display the returned cropped image
                picView.setImageBitmap(thePic);
            } else if (requestCode == 2404) {

                //Retrives the image server file path from Upload Activity using the request code.
                file_path = data.getStringExtra("file_path");
                Log.d(TAG, file_path);
                Toast.makeText(getActivity(), file_path, Toast.LENGTH_LONG).show();
                //Log.d(TAG,file_path);
            }
        }
    }

    private void launchUploadActivity() {
        //sends the android file path to the Upload Activity
        Intent i = new Intent(getActivity(), UploadActivity.class);
        i.putExtra("filePath", picturePath);

        startActivityForResult(i, 2404);

    }
}
