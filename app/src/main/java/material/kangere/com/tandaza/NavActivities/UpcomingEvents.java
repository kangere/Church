package material.kangere.com.tandaza.NavActivities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import material.kangere.com.tandaza.Adapters.EventAdapter;
import material.kangere.com.tandaza.EventData;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.ApiFields;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.CheckNetwork;
import material.kangere.com.tandaza.util.DetailsParcel;
import material.kangere.com.tandaza.util.RequestQueueSingleton;


public class UpcomingEvents extends Fragment implements EventAdapter.EventsClickListener {



    private RecyclerView recyclerView;
    private TextView noCon;


    private static final String TAG = UpcomingEvents.class.getSimpleName();

    private JSONArray json_events_cache = new JSONArray();
    private ArrayList<EventData> eventsList = new ArrayList<>();
    private EventAdapter adapter;

    private ProgressDialog dialog;

    private final int EVENTS_PROGRESS_DELAY = 1000;

    // products JSONArray


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.upcoming_events, container, false);

        noCon =  layout.findViewById(R.id.tvEventNoNetwork);

        //clear eventslist to avoid duplicating data
        eventsList.clear();


        recyclerView =  layout.findViewById(R.id.rvEvents);
        adapter = new EventAdapter(getActivity());
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //Load data to populate adapter
        loadData();

        FloatingActionButton upload = layout.findViewById(R.id.bCreateEvent);
        upload.setOnClickListener(
                view -> {
                    Create_Event create_event = new Create_Event();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.flContent, create_event)
                            .addToBackStack(create_event.getClass().getSimpleName())
                            .commit();

                });

        final SwipeRefreshLayout refreshLayout =  layout.findViewById(R.id.events_refreshLayout);

        refreshLayout.setOnRefreshListener(() -> {
            loadData();

            refreshLayout.setRefreshing(false);

        });
        return layout;
    }

    /**
     * Loads Data required to populate recyclerview
     */
    private void loadData() {

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading events...");
        dialog.show();

        //check if internet connection is available
        if (CheckNetwork.isInternetAvailable(getActivity())) {


            eventsList.clear();

            JsonObjectRequest objectRequest = new JsonObjectRequest(AppConfig.URL_GET_EVENTS, null,

                    response -> {

                    Log.d(TAG,response.toString());

                        try {
                            JSONArray array = response.getJSONArray(ApiFields.TAG_EVENTS);

                            for (int i = 0; i < array.length(); ++i) {
                                JSONObject c = array.optJSONObject(i);

                                // Storing each json item in variable
                                String id = c.getString(ApiFields.TAG_ID);
                                String title = c.getString(ApiFields.TAG_TITLE);
                                String date = c.getString(ApiFields.TAG_DATE);
                                String time = c.getString(ApiFields.TAG_TIME);
                                String posterpath = c.getString(ApiFields.TAG_POSTER);
                                String venue = c.getString(ApiFields.TAG_VENUE);
                                String ministry = c.getString(ApiFields.TAG_MINISTRY);
                                String description = c.getString(ApiFields.TAG_DESCRIPTION);

                                //storing each variable
                                EventData eventTitles = new EventData();

                                eventTitles.setId(id);
                                eventTitles.setTitle(title);
                                eventTitles.setDate(date);
                                eventTitles.setTime(time);
                                eventTitles.setMinistry(ministry);
                                eventTitles.setVenue(venue);
                                eventTitles.setPosterpath(posterpath);
                                eventTitles.setDescription(description);


                                Log.d("One Event: ", title);


                                eventsList.add(eventTitles);
                            }

                            adapter.setEventsList(eventsList);

                            //store Jsonarray in cache
                            File cacheDir =  getActivity().getCacheDir();
                            File file = new File(cacheDir.getAbsolutePath(),"events.txt");

                            //delete file if already exists
                            //avoids duplicate cache files
                            if (file.exists() && file.delete()) {
                                file = new File(cacheDir.getAbsolutePath(),"events.txt");
                            }

                            Log.d(TAG,array.toString());

                            //write cache data to file
                            try (FileOutputStream fos = new FileOutputStream(file)) {

                                fos.write(array.toString().getBytes());

                            }catch (IOException e) {
                                Log.e(TAG,e.getMessage());
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, e.toString());

                        }
                    },

                    error -> Log.e(TAG, error.getMessage())

            );

            try {
                RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(objectRequest);
                new Handler().postDelayed(() -> {
                /* Create an Intent that will start the Menu-Activity. */
                    dialog.dismiss();

                }, EVENTS_PROGRESS_DELAY);
            } catch (NullPointerException e) {
                Log.e(TAG, "null" + e.getMessage());
            }

        } else {

            //Alert user their is no internet connection
            Toast.makeText(getActivity(),"No Internet Connection", Toast.LENGTH_LONG).show();


            loadCache();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    dialog.dismiss();
                }
            }, EVENTS_PROGRESS_DELAY);

        }
    }

    /**
     * Function loads the local cache from databse
     * if their is no internet connection.
     */
    private void loadCache() {



        //get application cache dir
        File cacheDir = getActivity().getCacheDir();
        File file = new File(cacheDir.getAbsolutePath(),"events.txt");
        byte[] byteJSON = new byte[(int)file.length()];

        //read cache from dir
        try(FileInputStream fis = new FileInputStream(file))
        {
            if(fis.read(byteJSON)== -1){
                throw new IOException("EOF reached while reading file");
            }

        }catch (IOException  e)
        {
            Log.e(TAG,e.getMessage());
        }

        try {
            json_events_cache = new JSONArray(new String(byteJSON));
        }catch (JSONException e)
        {
            Log.e(TAG,e.getMessage());
        }


        //check if cache is empty
        if (!file.exists()) {
            //if cache is empty
            //prompt user to connect to internet atleast once
            recyclerView.setVisibility(View.GONE);
            noCon.setVisibility(View.VISIBLE);

        } else {
            //if cache is not null
            //load data from cache
            try {

                eventsList.clear();

                for (int i = 0; i < json_events_cache.length(); i++) {
                    JSONObject c = json_events_cache.optJSONObject(i);

                    // Storing each json item in variable
                    String title = c.getString(ApiFields.TAG_TITLE);
                    String date = c.getString(ApiFields.TAG_DATE);
                    String time = c.getString(ApiFields.TAG_TIME);
                    String posterpath = c.getString(ApiFields.TAG_POSTER);
                    String venue = c.getString(ApiFields.TAG_VENUE);
                    String ministry = c.getString(ApiFields.TAG_MINISTRY);
                    String description = c.getString(ApiFields.TAG_DESCRIPTION);


                    //storing each variable
                    EventData eventTitles = new EventData();


                    eventTitles.setTitle(title);
                    eventTitles.setDate(date);
                    eventTitles.setTime(time);
                    eventTitles.setMinistry(ministry);
                    eventTitles.setVenue(venue);
                    eventTitles.setPosterpath(posterpath);
                    eventTitles.setDescription(description);


//                    Log.d("One Event: ", title);

                    //getSupportActionBar().setIcon(new BitmapDrawable(getResources(), letterTile));


                    eventsList.add(eventTitles);

                   /* getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setEventsList(eventsList);
                        }
                    });*/

                }
                adapter.setEventsList(eventsList);

            } catch (JSONException e) {
                Log.d(TAG, e.toString());
            }
        }

    }

    @Override
    public void itemClicked(View view, int position) {
        // Toast.makeText(this, "Item " + position + " clicked", Toast.LENGTH_LONG).show();
        Log.d(TAG, "item " + position + " clicked");

        //get data from single card
        String id = ((TextView)view.findViewById(R.id.tvGoneEventId)).getText().toString();
        String title = ((TextView)view.findViewById(R.id.tvEventsTitle)).getText().toString();
        String imgpath = ((TextView)view.findViewById(R.id.tvGoneEventImgpath)).getText().toString();
        String date = ((TextView)view.findViewById(R.id.tvGoneEventDate)).getText().toString();
        String time = ((TextView)view.findViewById(R.id.tvGoneEventTime)).getText().toString();
        String ministry = ((TextView)view.findViewById(R.id.tvGoneEventMinistry)).getText().toString();
        String venue = ((TextView)view.findViewById(R.id.tvGoneEventVenue)).getText().toString();
        String description = ((TextView)view.findViewById(R.id.tvEventDescription)).getText().toString();

        Map<String,String> eventDetails = new HashMap<>();

        eventDetails.put(ApiFields.TAG_ID,id);
        eventDetails.put(ApiFields.TAG_TITLE,title);
        eventDetails.put(ApiFields.TAG_POSTER,imgpath);
        eventDetails.put(ApiFields.TAG_DATE,date);
        eventDetails.put(ApiFields.TAG_TIME,time);
        eventDetails.put(ApiFields.TAG_MINISTRY,ministry);
        eventDetails.put(ApiFields.TAG_VENUE,venue);
        eventDetails.put(ApiFields.TAG_DESCRIPTION,description);




        Intent intent = new Intent(getActivity(),ViewEvent.class);

        intent.putExtra("event_details",new DetailsParcel(eventDetails));

        startActivity(intent);




    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Events");
    }
}
