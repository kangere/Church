package material.kangere.com.tandaza.util;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import material.kangere.com.tandaza.ItemData;

public class StoriesViewModel extends AndroidViewModel {

    private static final String TAG = StoriesViewModel.class.getSimpleName();
    private List<ItemData> stories = new ArrayList<>();

    private Application application;


    public StoriesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        loadStories();
    }

    public List<ItemData> getStories() {
        return stories;
    }

    public void refresh(){
        stories.clear();
        loadStories();
    }

    private void loadStories(){

        if(!CheckNetwork.isInternetAvailable(application))
            return;



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(AppConfig.URL_GET_ALL_NOTIFICATIONS, null,
                response -> {

                        try {
                            JSONArray array = response.getJSONArray(ApiFields.TAG_STORIES_NOTIFICATIONS);


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject c = array.getJSONObject(i);

                                // Storing each json item in variable
                                String nid = c.getString(ApiFields.TAG_ID);
                                String title = c.getString(ApiFields.TAG_STORIES_TITLE);
                                String content = c.getString(ApiFields.TAG_STORIES_CONTENT);
                                String ministry = c.getString(ApiFields.TAG_MINISTRY);
                                String image_path = c.getString(ApiFields.TAG_STORIES_IMAGE_PATH);
                                String time_stamp = c.getString(ApiFields.TAG_STORIES_TIMESTAMP);

                                String timestamp = Dates.getTimeSpan(time_stamp);


                                ItemData notificationsTitles = new ItemData();

                                notificationsTitles.setNid(nid);
                                notificationsTitles.setTitle(title);
                                notificationsTitles.setContent(content);
                                notificationsTitles.setMinistry(ministry);
                                notificationsTitles.setImagePath(image_path);
                                notificationsTitles.setTime_stamp(timestamp);

                                stories.add(notificationsTitles);
                            }



                            //store data in cache
                            File cacheDir = application.getCacheDir();

                            File file = new File(cacheDir.getAbsolutePath(),"stories.txt");

                            //delete file if already exists
                            //avoids duplicate cache files
                            if (file.exists() && file.delete()) {
                                file = new File(cacheDir.getAbsolutePath(),"stories.txt");
                            }
                            Log.d(TAG,array.toString());
                            //write cache data to file
                            try (FileOutputStream fos = new FileOutputStream(file))
                            {

                                fos.write(array.toString().getBytes());

                            } catch (IOException e) {
                                Log.e(TAG,e.getMessage());
                            }

                        } catch(JSONException e){

                            Log.e(TAG,e.toString());
                        }
                    },
                error -> Log.e(TAG,error.toString()));


        RequestQueueSingleton.getInstance(application).addToRequestQueue(jsonObjectRequest);


    }
}
