package material.kangere.com.tandaza.NavActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import material.kangere.com.tandaza.Adapters.EventAdapter;
import material.kangere.com.tandaza.AppConfig;
import material.kangere.com.tandaza.CheckNetwork;
import material.kangere.com.tandaza.EventData;
import material.kangere.com.tandaza.JSONParser;
import material.kangere.com.tandaza.LocalDB.SQLiteHandler;
import material.kangere.com.tandaza.LocalDB.TablesContract;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.StaticMethods;


public class UpcomingEvents extends AppCompatActivity implements EventAdapter.EventsClickListener {

    ProgressDialog progressDialog;
    private SQLiteHandler db;
    private RecyclerView recyclerView;
    private TextView noCon;
    private JSONParser jParser = new JSONParser();
    private JSONArray json_events_cache;
    private static final String TAG = UpcomingEvents.class.getSimpleName();

    private ArrayList<EventData> eventsList = new ArrayList<>();
    private EventAdapter adapter;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_NAME = "event_name";
    private static final String TAG_DATE = "event_date";
    private static final String TAG_TIME = "event_time";
    private static final String TAG_MINISTRY = "ministry";
    private static final String TAG_POSTER = "posterpath";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DESCRIPTION = "description";

    // products JSONArray
    private JSONArray events = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events);


        noCon =(TextView) findViewById(R.id.tvEventsNoNetwork);
        StaticMethods.ClassInitisialisation(this, R.id.events_fragment, R.id.upcoming_eventsToolbar, R.id.dlEvenets);
        db = new SQLiteHandler(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.rvEvents);
        adapter = new EventAdapter(getBaseContext());
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        if (CheckNetwork.isInternetAvailable(UpcomingEvents.this)) {

            try {
                new EventsLoader().execute();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            recyclerView.setVisibility(View.VISIBLE);
            noCon.setVisibility(View.GONE);

        } else {
            LoadDB();
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

    /*
     *@definition - Function loads the local cache from databse
                    if their is no internet connection.
     */
    private void LoadDB() {
        SQLiteDatabase database = db.getReadableDatabase();

        String eventQuery = "SELECT * FROM " + TablesContract.EventsEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(eventQuery, null);
        cursor.moveToFirst();
        //check if cache is empty
        if (cursor.isNull(cursor.getColumnIndex(TablesContract.EventsEntry.COLUMN_EVENT_CACHE))) {
            //if cache is empty
            //prompt user to connect to internet atleast once
            recyclerView.setVisibility(View.GONE);
            noCon.setVisibility(View.VISIBLE);

        } else {
            //if cache is not null
            //load data from cache
            try {
                HashMap<String, String> note_cache = db.getEventCacheDetails();


                String eventcache = note_cache.get("event_cache");

                JSONObject json = new JSONObject(eventcache);
                json_events_cache = json.optJSONArray("events");

                Log.d("All Events: ", json.toString());


                for (int i = 0; i < json_events_cache.length(); i++) {
                    JSONObject c = json_events_cache.optJSONObject(i);

                    // Storing each json item in variable
                    String title = c.getString(TAG_NAME);
                    String date = c.getString(TAG_DATE);
                    String time = c.getString(TAG_TIME);
                    String posterpath = c.getString(TAG_POSTER);
                    String venue = c.getString(TAG_VENUE);
                    String ministry = c.getString(TAG_MINISTRY);
                    String description = c.getString(TAG_DESCRIPTION);


                    //storing each variable
                    EventData eventTitles = new EventData();


                    eventTitles.setTitle(title);
                    eventTitles.setDate(date);
                    eventTitles.setTime(time);
                    eventTitles.setMinistry(ministry);
                    eventTitles.setVenue(venue);
                    eventTitles.setPosterpath(posterpath);
                    eventTitles.setDescription(description);


                    Log.d("One Event: ", title);

                    //getSupportActionBar().setIcon(new BitmapDrawable(getResources(), letterTile));


                    eventsList.add(eventTitles);

                    adapter.setEventsList(eventsList);

                }

            } catch (JSONException e) {
                Log.d(TAG, e.toString());
            }
        }
        cursor.close();
    }

    @Override
    public void itemClicked(View view, int position) {
        // Toast.makeText(this, "Item " + position + " clicked", Toast.LENGTH_LONG).show();
        Log.d(TAG, "item " + position + " clicked");
    }


    public void CreateEvent(View view) {
        startActivity(new Intent(this, Create_Event.class));
    }

    private class EventsLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = Show_Notifications.createProgrssDialog(UpcomingEvents.this);
                progressDialog.show();
            } else {
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(AppConfig.URL_GET_EVENTS, "GET", params);

            if (json != null) {
                Log.d("All events: ", json.toString());
                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        events = json.getJSONArray(TAG_EVENTS);

                        // looping through All Products
                        for (int i = 0; i < events.length(); i++) {
                            JSONObject c = events.getJSONObject(i);

                            // Storing each json item in variable
                            String name = c.getString(TAG_NAME);
                            String date = c.getString(TAG_DATE);
                            String time = c.getString(TAG_TIME);
                            String poster = c.getString(TAG_POSTER);
                            String ministry = c.getString(TAG_MINISTRY);
                            String venue = c.getString(TAG_VENUE);
                            String description = c.getString(TAG_DESCRIPTION);



                           /* try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                parsedDate = dateFormat.parse(time_stamp);
                            } catch (Exception e) {//this generic but you can control another types of exception
                                e.printStackTrace();
                            }
                            DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), NOW, DateUtils.MINUTE_IN_MILLIS);

                            String timestamp = String.valueOf(DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), NOW, DateUtils.MINUTE_IN_MILLIS));
                            */
                            //storing each variable
                            EventData eventsTitles = new EventData();

                            eventsTitles.setTitle(name);
                            eventsTitles.setPosterpath(poster);
                            eventsTitles.setDate(date);
                            eventsTitles.setTime(time);
                            eventsTitles.setMinistry(ministry);
                            eventsTitles.setVenue(venue);
                            eventsTitles.setDescription(description);


                            eventsList.add(eventsTitles);
                            json.put("events_cache", new JSONArray(eventsList));
                            String arrayList = json.toString();

                            db.updateEventsCache(arrayList);
                        }
                    } else {
                        Log.d(TAG, "Error retreiving json object");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                LoadDB();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpcomingEvents.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setEventsList(eventsList);
                }
            });
        }
    }
}
