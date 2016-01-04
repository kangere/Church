package material.kangere.com.tandaza.NavActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import material.kangere.com.tandaza.Adapters.MyAdapter;
import material.kangere.com.tandaza.AppConfig;
import material.kangere.com.tandaza.CheckNetwork;
import material.kangere.com.tandaza.ItemData;
import material.kangere.com.tandaza.JSONParser;
import material.kangere.com.tandaza.LocalDB.SQLiteHandler;
import material.kangere.com.tandaza.MakeNotification;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.SessionManager;
import material.kangere.com.tandaza.StaticMethods;

public class Show_Notifications extends AppCompatActivity implements MyAdapter.ClickListener {

    private static final String TAG = Show_Notifications.class.getSimpleName();


    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();

    //array list to be passed into recycler view adapter
    private ArrayList<ItemData> notificationsList = new ArrayList<>();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_NID = "id";
    private static final String TAG_CONTENT = "contents";
    private static final String TAG_MINISTRY = "ministry";
    private static final String TAG_TITLE = "title";
    private static final String TAG_IMAGE_PATH = "imgpath";
    private static final String TAG_TIMESTAMP = "created_at";

    // products JSONArray
    private JSONArray notifications = null;
    private JSONArray json_notification_cache;
    private SessionManager session;
    private SQLiteHandler db;
    public RecyclerView recyclerView;


    private MyAdapter adapter;
    private long NOW = new Date().getTime();
    private Date parsedDate;

    private LinearLayout linearLayoutCon;

    private Button btnUploadClass, refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show__notifications);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //toolbar and navigation bar initialisation
        StaticMethods.ClassInitisialisation(this, R.id.Show_Note_fragment_navigation_drawer, R.id.tbShowNote, R.id.Show_Note_drawer_layout);

        //Upload button initialisation
        btnUploadClass = (Button) findViewById(R.id.bOpenUploadClass);
        btnUploadClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Show_Notifications.this, MakeNotification.class));
            }
        });
        //db initialisation
        db = new SQLiteHandler(getApplicationContext());

        //recycler view initialisation
        recyclerView = (RecyclerView) findViewById(R.id.rvShowNote);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MyAdapter(getBaseContext());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        loadData();


    }



    private void loadData() {
        //check to see if internet connection is available
        if (CheckNetwork.isInternetAvailable(Show_Notifications.this)) //returns true if internet available
        {
            linearLayoutCon = (LinearLayout) findViewById(R.id.lLConnection);
            linearLayoutCon.setVisibility(View.VISIBLE);
            try {
                new LoadAllNotifications().execute();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        } else {//if not load data from cache in local database
            LoadCache();
            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG);

            snack.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            });
            snack.setActionTextColor(getResources().getColor(R.color.accent_color));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();


        }
    }

    public static ProgressDialog createProgrssDialog(Context mContext){
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
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

    private void LoadCache() {

        try {
            HashMap<String, String> note_cache = db.getNotificationCache();


            String notecache = note_cache.get("notification_cache");

            JSONObject json = new JSONObject(notecache);
            json_notification_cache = json.optJSONArray("notifications");

            Log.d("All Notifications: ", json.toString());


            for (int i = 0; i < json_notification_cache.length(); i++) {
                JSONObject c = json_notification_cache.optJSONObject(i);

                // Storing each json item in variable
                String nid = c.getString(TAG_NID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String ministry = c.getString(TAG_MINISTRY);
                String image_path = c.getString(TAG_IMAGE_PATH);
                String time_stamp = c.getString(TAG_TIMESTAMP);


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
                notificationsTitles.setContent(content);
                notificationsTitles.setMinistry(ministry);
                notificationsTitles.setImagePath(image_path);
                notificationsTitles.setTime_stamp(timestamp);


                Log.d("One Notification: ", title);
                notificationsList.add(notificationsTitles);

                adapter.setNotificationsList(notificationsList);

            }

        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void itemClicked(View view, int position) {

        String nid = ((TextView) view.findViewById(R.id.nid)).getText()
                .toString();
        Intent i = new Intent(Show_Notifications.this, ViewNotification.class);
        //i.putExtra(TAG_NID, nid);

        String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
        String timeStamp = ((TextView) view.findViewById(R.id.notificationTimeStamp)).getText().toString();
        String ministry = ((TextView) view.findViewById(R.id.tvMinistryGone)).getText().toString();
        String content = ((TextView) view.findViewById(R.id.tvContentGone)).getText().toString();
        String img_path = ((TextView) view.findViewById(R.id.tvImgPathGone)).getText().toString();

        i.putExtra(TAG_TITLE, title);
        i.putExtra(TAG_MINISTRY, ministry);
        i.putExtra(TAG_CONTENT, content);
        i.putExtra(TAG_TIMESTAMP, timeStamp);
        i.putExtra(TAG_IMAGE_PATH, img_path);

        startActivity(i);

    }

    class LoadAllNotifications extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {
                pDialog = createProgrssDialog(Show_Notifications.this);
                pDialog.show();
            } else {
                pDialog.show();
            }
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(AppConfig.URL_GET_ALL_NOTIFICATIONS, "GET", params);

            if (json != null) {
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
                            String content = c.getString(TAG_CONTENT);
                            String ministry = c.getString(TAG_MINISTRY);
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
                            notificationsTitles.setContent(content);
                            notificationsTitles.setMinistry(ministry);
                            notificationsTitles.setImagePath(image_path);
                            notificationsTitles.setTime_stamp(timestamp);


                            notificationsList.add(notificationsTitles);

                            json.put("notification_cache", new JSONArray(notificationsList));
                            String arrayList = json.toString();

                            db.updateNotificationCache(arrayList);
                        }
                    } else {
                        Log.d(TAG, "Error retreiving json object");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                LoadCache();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Show_Notifications.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                });
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
