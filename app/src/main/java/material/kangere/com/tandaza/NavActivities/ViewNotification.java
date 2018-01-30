package material.kangere.com.tandaza.NavActivities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import material.kangere.com.tandaza.JSONParser;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.ApiFields;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.RequestQueueSingleton;


public class ViewNotification extends Fragment {

    private final String TAG = ViewNotification.class.getSimpleName();
    private TextView title, content, ministry, timeStamp;
    private ImageView sourceImage;
    private static final String NOTE_ID = "note_array";
    private String[] notification;
    private String id;
    private Button delete,update;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    public ViewNotification() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.view_note);


        //get arguments from activity to display
        notification = getArguments().getStringArray(NOTE_ID);

       /* Intent i = getIntent();
        String titleDummy = i.getExtras().getString("title");
        String ministryString = i.getStringExtra("ministry");
        String contentString = i.getStringExtra("contents");
        String timeStampString = i.getStringExtra("created_at");
        String imgPath = i.getStringExtra("imgpath");

        Uri uri = Uri.parse(imgPath);

        ministry.setText(ministryString);
        content.setText(contentString);
        timeStamp.setText(timeStampString);
        title.setText(titleDummy);
       // Log.d("Title testing", titleDummy);
        try {
            Glide.with(this).load(uri).into(sourceImage);
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_note, container, false);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        } catch (NullPointerException e) {
            Log.e("ViewNote", e.toString());
        }
        title = layout.findViewById(R.id.tvDetailNoteTitle);
        content =  layout.findViewById(R.id.tvDetailNoteContent);
        ministry =  layout.findViewById(R.id.tvDetailMinistry);
        timeStamp =  layout.findViewById(R.id.tvDetailTimeStamp);
        sourceImage = layout.findViewById(R.id.ivDetailNoteImage);
        delete =  layout.findViewById(R.id.bDeleteNote);
        update = layout.findViewById(R.id.bUpdateNote);


        if (notification != null) {
            String titleDummy = notification[0];
            String timeStampString = notification[1];
            String ministryString = notification[2];
            String contentString = notification[3];
            String imgPath = notification[4];
            id = notification[5];


            Uri uri = Uri.parse(imgPath);

            ministry.setText(ministryString);
            content.setText(contentString);
            timeStamp.setText(timeStampString);
            title.setText(titleDummy);

            try {
                Glide.with(this).load(uri).into(sourceImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new NullPointerException(getActivity().toString()
                    + " array must not be null or empty");

        }

        //button operations
        delete.setOnClickListener((view)-> {
//                new Delete().execute();
                delete_note();
        }
        );

        update.setOnClickListener((view)-> {

            //go to previous fragment
            UpdateNote updateNote = new UpdateNote();

            Bundle args = new Bundle();
            args.putStringArray("note_array",notification);

            updateNote.setArguments(args);

            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.flContent, updateNote);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Details");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Show_Notifications show_notifications = new Show_Notifications();
                getFragmentManager().beginTransaction()
                        .replace(R.id.flContent, show_notifications)
                        .addToBackStack(show_notifications.getClass().getSimpleName())
                        .commit();

                getActivity().setTitle("News");
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void delete_note()
    {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE,
                response ->{

                    Log.d(TAG, response);


                    int success = 0;


                    try {
                        JSONObject temp = new JSONObject(response);
                        success = temp.getInt(ApiFields.TAG_SUCCESS);

                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }

                    if (success == 1) {


                        Toast.makeText(getActivity(), "Notification deleted successfully", Toast.LENGTH_LONG).show();

                        //go to previous fragment
                        Show_Notifications showNotifications = new Show_Notifications();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.flContent, showNotifications);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();

                    } else {

                        Toast.makeText(getActivity(), "Failed to create notification, Try Again", Toast.LENGTH_LONG).show();

                    }

                },
                error -> Log.d(TAG,error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);

                return params;
            }
        };

        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    /*class Delete extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {
                pDialog = new CustomProgressDialog(getActivity(),TAG);
                pDialog.show();
            } else {
                pDialog.show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id",id));

            JSONObject jsonObject = jsonParser.makeHttpRequest(AppConfig.URL_DELETE,"POST",params);
            Log.d("ViewNote", id);

            try {
                int success = jsonObject.getInt("success");
                Log.d("ViewNote","" + success );

                if (success == 1){

                    boolean notDeleted = jsonObject.getBoolean("error");
                        if(!notDeleted)
                        {
                            //go to previous fragment
                            Show_Notifications showNotifications = new Show_Notifications();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

                            // Replace whatever is in the fragment_container view with this fragment,
                            // and add the transaction to the back stack
                            transaction.replace(R.id.flContent, showNotifications);
                            transaction.addToBackStack(null);

                            // Commit the transaction
                            transaction.commit();
                        }else {

                            Log.e("ViewNote","Deleting Failed");
                        }


                }else {
                    Log.e("Error", "Json request failed");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Something Seems to be wrong, Try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }catch (JSONException e){
                Log.d("ViewNote", e.toString());
            }

            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
        }
    }
*/

}
