package material.kangere.com.tandaza.NavActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import material.kangere.com.tandaza.R;


public class UpcomingEvents extends AppCompatActivity implements EventAdapter.EventsClickListener {

    ProgressDialog progressDialog;
    private SQLiteHandler db;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
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
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DESCRIPTION = "description";

    // products JSONArray
    private JSONArray events = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clEvents);
        toolbar = (Toolbar) findViewById(R.id.upcoming_eventsToolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

        } else {
            LoadDB();
            Snackbar snack = Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_LONG);

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
            TextView tv =(TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();



        }


    }

    private  void LoadDB() {
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

    @Override
    public void itemClicked(View view, int position) {
        Toast.makeText(this, "Item " + position + " clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upcoming_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public void CreateEvent(View view) {
        startActivity(new Intent(this, Create_Event.class));
    }

    private class EventsLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpcomingEvents.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //pDialog.setMessage("Loading notifications. Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);

            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
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
