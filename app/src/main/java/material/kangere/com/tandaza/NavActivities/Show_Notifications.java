package material.kangere.com.tandaza.NavActivities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import material.kangere.com.tandaza.Adapters.MyAdapter;
import material.kangere.com.tandaza.ItemData;
import material.kangere.com.tandaza.MakeNotification;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.ApiFields;


public class Show_Notifications extends Fragment implements MyAdapter.ClickListener{

    private static final String TAG = Show_Notifications.class.getSimpleName();

    // products JSONArray
    private JSONArray json_notification_cache = new JSONArray();
    private TextView noCon;
    private LinearLayout connection;


    private MyAdapter adapter;
    private long NOW = new Date().getTime();
    private Date parsedDate;
    private ProgressDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.content_show__notifications, container, false);


        noCon =  layout.findViewById(R.id.tvShowNoteNoConnection);
        connection = layout.findViewById(R.id.lLConnection);



        FloatingActionButton openCreateNote = layout.findViewById(R.id.bUploadNote);

        openCreateNote.setOnClickListener(
                view -> {
                    MakeNotification makeNotification = new MakeNotification();

                    getFragmentManager().beginTransaction()
                            .replace(R.id.flContent, makeNotification)
                            .addToBackStack(makeNotification.getClass().getSimpleName())
                            .commit();
                }
        );


        RecyclerView recyclerView = layout.findViewById(R.id.rvShowNote);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyAdapter(getActivity());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        loadData();

        final SwipeRefreshLayout refreshLayout =  layout.findViewById(R.id.note_swipeRefresh);

        refreshLayout.setOnRefreshListener(
                () -> {
                Log.i(TAG, "Refreshing RecyclerView");

                loadData();

                refreshLayout.setRefreshing(false);

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

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void loadData() {


        List<ItemData> stories = MainActivity.getViewModel().getStories();

        if (stories.size() > 0) {

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading news...");
            dialog.show();



            adapter.setNotificationsList(stories);

            new Handler().postDelayed(
                    () -> {

                        dialog.dismiss();

                    }, 1000);
        } else {


            Toast.makeText(getActivity(),"No Internet Connection", Toast.LENGTH_LONG).show();

            LoadCache();

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




    private void LoadCache() {

        //using cache dir
        File cacheDir = getActivity().getCacheDir();
        File file = new File(cacheDir.getAbsolutePath(),"stories.txt");
        byte[] byteJSON = new byte[(int)file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {

            if(fis.read(byteJSON)== -1){
                throw new IOException("EOF reached while reading file");
            }

        }catch (IOException  e) {
            Log.e(TAG,e.getMessage());
        }


        try {
            json_notification_cache = new JSONArray(new String(byteJSON));
        }catch (JSONException e)
        {
            Log.e(TAG,e.getMessage());
        }



        if (!file.exists()) {
            connection.setVisibility(View.GONE);
            noCon.setVisibility(View.VISIBLE);


        } else {
            try {

                List<ItemData> stories = new ArrayList<>();


                for (int i = 0; i < json_notification_cache.length(); i++) {
                    JSONObject jsonObject = json_notification_cache.optJSONObject(i);

                    // Storing date json item in variable
                    String time_stamp = jsonObject.getString(ApiFields.TAG_STORIES_TIMESTAMP);


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

                    notificationsTitles.setNid(jsonObject.getString(ApiFields.TAG_ID));
                    notificationsTitles.setTitle(jsonObject.getString(ApiFields.TAG_STORIES_TITLE));
                    notificationsTitles.setContent(jsonObject.getString(ApiFields.TAG_STORIES_CONTENT));
                    notificationsTitles.setMinistry(jsonObject.getString(ApiFields.TAG_MINISTRY));
                    notificationsTitles.setImagePath(jsonObject.getString(ApiFields.TAG_STORIES_IMAGE_PATH));
                    notificationsTitles.setTime_stamp(timestamp);

                    stories.add(notificationsTitles);

                }
                adapter.setNotificationsList(stories);

            } catch (JSONException e) {
                Log.d(TAG, e.toString());
            }
        }



    }

    /**
     * Opens the viewNotification fragment when a user clicks the notification.
     *
     * @param view     - the view holding the UI elements in the individual notification
     * @param position - position of the notification clicked
     */
    @Override
    public void itemClicked(View view, int position) {

        final String NOTE_ID = "note_array";
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

}
