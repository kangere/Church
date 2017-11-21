package material.kangere.com.tandaza.NavActivities;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import material.kangere.com.tandaza.Adapters.MyAdapter;
import material.kangere.com.tandaza.ItemData;
import material.kangere.com.tandaza.LocalDB.SQLiteHandler;
import material.kangere.com.tandaza.LocalDB.TablesContract;
import material.kangere.com.tandaza.MakeNotification;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.CheckNetwork;
import material.kangere.com.tandaza.util.RequestQueueSingleton;


public class Show_Notifications extends Fragment implements MyAdapter.ClickListener {

    private static final String TAG = Show_Notifications.class.getSimpleName();


    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object


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
    private SQLiteHandler db;
    private RecyclerView recyclerView;
    private TextView noCon;
    private LinearLayout connection;


    private MyAdapter adapter;
    private long NOW = new Date().getTime();
    private Date parsedDate;
    private final String NOTE_ID = "note_array";
    private Button btnUploadClass;
    private ProgressDialog dialog;

    private final int NOTIFICATION_PROGRESS_DELAY = 1000;

    /* public interface onNotificationSelectedListener{
          void NotificationSelected(int position,String[] content);
     }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.content_show__notifications, container, false);

        //clear the notification list
        notificationsList.clear();


        //Connection/No Connection User Interfaces
        noCon = (TextView) layout.findViewById(R.id.tvShowNoteNoConnection);
        connection = (LinearLayout) layout.findViewById(R.id.lLConnection);


        //Upload button initialisation
        btnUploadClass = (Button) layout.findViewById(R.id.bOpenUploadClass);
        btnUploadClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Show_Notifications.this, MakeNotification.class));
                MakeNotification makeNotification = new MakeNotification();
                getFragmentManager().beginTransaction()
                        .replace(R.id.flContent, makeNotification)
                        .addToBackStack(makeNotification.getClass().getSimpleName())
                        .commit();
            }
        });

        //db initialisation
        db = new SQLiteHandler(getActivity());

        //recycler view initialisation
        recyclerView = (RecyclerView) layout.findViewById(R.id.rvShowNote);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyAdapter(getActivity());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //used to populate recyclerview
        loadData();

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.note_swipeRefresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Refreshing RecyclerView");

                loadData();

                refreshLayout.setRefreshing(false);
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("News");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            //notificationSelectedListener = (onNotificationSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void loadData() {
        //check to see if internet connection is available

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading news...");
        dialog.show();

        if (CheckNetwork.isInternetAvailable(getActivity())) {
            notificationsList.clear();

            /*try {
                new LoadAllNotifications().execute();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                connection.setVisibility(View.VISIBLE);
                noCon.setVisibility(View.GONE);

            }*/

            //Volley handles network requests
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(AppConfig.URL_GET_ALL_NOTIFICATIONS, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray array = response.getJSONArray(TAG_NOTIFICATIONS);

                                JSONObject forCache = new JSONObject();

                                File temp = getActivity().getCacheDir();

                                try (OutputStream outputStream = new FileOutputStream(temp))
                                {


                                    
                                } catch(Exception e){

                                }


                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject c = array.getJSONObject(i);

                                    // Storing each json item in variable
                                    String nid = c.getString(TAG_NID);
                                    String title = c.getString(TAG_TITLE);
                                    String content = c.getString(TAG_CONTENT);
                                    String ministry = c.getString(TAG_MINISTRY);
                                    String image_path = c.getString(TAG_IMAGE_PATH);
                                    String time_stamp = c.getString(TAG_TIMESTAMP);


                                    try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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

                                    //stores json in db to be used if no internet connection found
                                    //TODO find better implementation for cache
                                    forCache.put("notification_cache", new JSONArray(notificationsList));
                                    String arrayList = forCache.toString();

                                    Log.d("arrayList", arrayList);
                                    db.updateNotificationCache(arrayList);
                                }

                                adapter.setNotificationsList(notificationsList);


                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });

            RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    dialog.dismiss();
                }
            }, NOTIFICATION_PROGRESS_DELAY);


        } else {//if not load data from cache in local database
            LoadCache();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    dialog.dismiss();
                }
            }, NOTIFICATION_PROGRESS_DELAY);

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
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }

    }


    /*
    Function to load stored cache from the local database if device is not connected to the internet
     */

    private void LoadCache() {

        String query = "SELECT * FROM " + TablesContract.NotificationsCache.TABLE_NAME;

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        if (cursor.isNull(cursor.getColumnIndex(TablesContract.NotificationsCache.COLUMN_NOTE_CACHE))) {
            connection.setVisibility(View.GONE);
            noCon.setVisibility(View.VISIBLE);


        } else {
            try {
                HashMap<String, String> note_cache = db.getNotificationCache();


                String notecache = note_cache.get("notification_cache");

                JSONObject json = new JSONObject(notecache);
                json_notification_cache = json.optJSONArray("notifications");

                Log.d("All Notifications: ", json.toString());


                for (int i = 0; i < json_notification_cache.length(); i++) {
                    JSONObject jsonObject = json_notification_cache.optJSONObject(i);

                    // Storing date json item in variable
                    String time_stamp = jsonObject.getString(TAG_TIMESTAMP);


                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        parsedDate = dateFormat.parse(time_stamp);
                    } catch (ParseException e) {//this generic but you can control another types of exception
                        Log.e(TAG, e.getMessage());
                    }
                    //DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), NOW, DateUtils.MINUTE_IN_MILLIS);

                    String timestamp = String.valueOf(DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), NOW, DateUtils.MINUTE_IN_MILLIS));

                    //storing each variable
                    ItemData notificationsTitles = new ItemData();

                    notificationsTitles.setNid(jsonObject.getString(TAG_NID));
                    notificationsTitles.setTitle(jsonObject.getString(TAG_TITLE));
                    notificationsTitles.setContent(jsonObject.getString(TAG_CONTENT));
                    notificationsTitles.setMinistry(jsonObject.getString(TAG_MINISTRY));
                    notificationsTitles.setImagePath(jsonObject.getString(TAG_IMAGE_PATH));
                    notificationsTitles.setTime_stamp(timestamp);

                    notificationsList.add(notificationsTitles);

                    /*getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setNotificationsList(notificationsList);
                        }
                    });*/

                }
                adapter.setNotificationsList(notificationsList);

            } catch (JSONException e) {
                Log.d(TAG, e.toString());
            }
        }
        cursor.close();


    }

    /**
     * Opens the viewNotification fragment when a user clicks the notification.
     *
     * @param view     - the view holding the UI elements in the individual notification
     * @param position - position of the notification clicked
     */
    @Override
    public void itemClicked(View view, int position) {

        //retrieve notification details once clicked
        String id = ((TextView) view.findViewById(R.id.nid)).getText().toString();
        String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
        String timeStamp = ((TextView) view.findViewById(R.id.notificationTimeStamp)).getText().toString();
        String ministry = ((TextView) view.findViewById(R.id.tvMinistryGone)).getText().toString();
        String content = ((TextView) view.findViewById(R.id.tvContentGone)).getText().toString();
        String img_path = ((TextView) view.findViewById(R.id.tvImgPathGone)).getText().toString();

        //single notification content in an array
        String[] notification = {title, timeStamp, ministry, content, img_path, id};

        //create detail note fragment
        ViewNotification viewNotification = new ViewNotification();
        //create bundle variable
        Bundle args = new Bundle();

        //store single notification in bundle and set viewNotification arguments to bundle
        args.putStringArray(NOTE_ID, notification);
        viewNotification.setArguments(args);

        //begin fragment transaction
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        // Commit the transaction
        getFragmentManager().beginTransaction()
                .replace(R.id.flContent, viewNotification)
                .addToBackStack(null)
                .commit();

        //setTitle("Story");

        try {
            getActivity().getActionBar().setHomeButtonEnabled(true);
        } catch (NullPointerException e) {
            Log.d("Show_Notification", " " + e);
        }

    }

    //backgound thread
    /*private class LoadAllNotifications extends AsyncTask<String, String, String> {

        *//**
     * Before starting background thread Show Progress Dialog
     *//*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {
                pDialog = new CustomProgressDialog(getActivity(), TAG);
                pDialog.show();
            } else {
                pDialog.show();
            }
        }


        *//**
     * getting All products from url
     *//*
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
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

                            Log.d("arrayList", arrayList);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                });
            }
            json = null;


            return null;
        }

        *//**
     * After completing background task Dismiss the progress dialog
     * *
     *//*
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    */

    /**
     * Updating parsed JSON data into ListView
     *//*
                    adapter.setNotificationsList(notificationsList);


                }
            });


        }


    }*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
