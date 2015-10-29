package material.kangere.com.tandaza;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import material.kangere.com.tandaza.LocalDB.SQLiteHandler;

public class Show_Notifications extends AppCompatActivity implements MyAdapter.ClickListener {

    private static final String TAG = Show_Notifications.class.getSimpleName();
    Toolbar bar;
    ListView noteListView;

    // Progress Dialog
    private ProgressDialog pDialog;

    MakeNotification just;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<ItemData> notificationsList = new ArrayList<>();

    // url to get all products list
    private static String url_all_notifications = "http://stuplan.co.ke/database_connect/get_all_notifications.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_NID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_IMAGE_PATH = "imgpath";
    private static final String TAG_TIMESTAMP = "created_at";

    // products JSONArray
    JSONArray notifications = null;
    Button addNote;
    private SessionManager session;
    private SQLiteHandler db;
    public RecyclerView recyclerView;


    private MyAdapter adapter;
    private long NOW = new Date().getTime();
    private Date parsedDate;
    private Date currentDate;
    private LinearLayout linearLayoutCon;
    private LinearLayout linearLayoutNoCon;
    private TextView noConnection;
    private Button btnUploadClass,refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show__notifications);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //toolbar and nav bar initialisation
        InitToolbar.ClassInitisialisation(this,R.id.Show_Note_fragment_navigation_drawer,R.id.tbShowNote,R.id.Show_Note_drawer_layout);

        btnUploadClass = (Button)findViewById(R.id.bOpenUploadClass);
        btnUploadClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Show_Notifications.this,MakeNotification.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rvShowNote);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MyAdapter(getBaseContext());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        if (CheckNetwork.isInternetAvailable(Show_Notifications.this)) //returns true if internet available
        {
            linearLayoutCon = (LinearLayout) findViewById(R.id.lLConnection);
            linearLayoutCon.setVisibility(View.VISIBLE);
            try {
                new LoadAllNotifications().execute();
            }catch(RuntimeException e){
                e.printStackTrace();
            }
            //do something. loadwebview.
        } else {
            Toast.makeText(Show_Notifications.this, "No Internet Connection", Toast.LENGTH_LONG).show();

            linearLayoutNoCon = (LinearLayout) findViewById(R.id.lLNoConnection);
            linearLayoutNoCon.setVisibility(View.VISIBLE);

            noConnection = (TextView) findViewById(R.id.tvShowNoteNoConnection);
            refresh = (Button) findViewById(R.id.bRefreshNotifications);

            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                }
            });


        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void itemClicked(View view, int position) {

        String nid = ((TextView) view.findViewById(R.id.nid)).getText()
                .toString();
        Intent i = new Intent(Show_Notifications.this, ViewNotification.class);
        i.putExtra(TAG_NID, nid);
        startActivity(i);

    }
    class LoadAllNotifications extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Show_Notifications.this);
            pDialog.setMessage("Loading notifications. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(AppConfig.URL_GET_ALL_NOTIFICATIONS, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Notifications: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    notifications = json.getJSONArray(TAG_NOTIFICATIONS);

                    // looping through All Products
                    for (int i = 0; i < notifications.length(); i++) {
                        JSONObject c = notifications.getJSONObject(i);

                        // Storing each json item in variable
                        String nid = c.getString(TAG_NID);
                        String title = c.getString(TAG_TITLE);
                        String image_path = c.getString(TAG_IMAGE_PATH);
                        String time_stamp = c.getString(TAG_TIMESTAMP);

                        //Toast.makeText(NotificationsActivity.this,nid+title,Toast.LENGTH_LONG).show();

                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            parsedDate = dateFormat.parse(time_stamp);
                        } catch (Exception e) {//this generic but you can control another types of exception
                            e.printStackTrace();
                        }
                        DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), NOW, DateUtils.MINUTE_IN_MILLIS);

                        String timestamp = String.valueOf(DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), NOW, DateUtils.MINUTE_IN_MILLIS));

                        //storing each variable
                        ItemData notificationsTitles = new ItemData();

                        notificationsTitles.setNid(nid);
                        notificationsTitles.setTitle(title);
                        notificationsTitles.setImagePath(image_path);
                        notificationsTitles.setTime_stamp(timestamp);


                        notificationsList.add(notificationsTitles);
                    }
                } else {


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    /**
                     * Updating parsed JSON data into ListView
                     * */


                    adapter.setNotificationsList(notificationsList);



                     /*adapter = new SimpleAdapter(
                            NotificationsActivity.this, notificationsList,
                            R.layout.list_cell, new String[]{TAG_NID,
                            TAG_TITLE},
                            new int[]{R.id.nid, R.id.title});
                    // updating listview
                    noteListView.setAdapter(adapter);
                    */


                }
            });


        }


    }

}
