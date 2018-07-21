package material.kangere.com.tandaza.NavActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.ApiFields;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.DetailsParcel;
import material.kangere.com.tandaza.util.RequestQueueSingleton;


public class ViewNotification extends Fragment {

    private final String TAG = ViewNotification.class.getSimpleName();


    private Map details;
    private String id;
    private ProgressDialog pDialog;
//    private JSONParser jsonParser = new JSONParser();

    public ViewNotification() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get arguments from activity to display


        DetailsParcel parcel = getArguments().getParcelable("note_details");

        if(parcel != null)
            details = parcel.getMap();
        else
            Log.e(TAG, "Error Parcel Empty");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_note, container, false);


        TextView title = layout.findViewById(R.id.tvDetailNoteTitle);
        TextView content = layout.findViewById(R.id.tvDetailNoteContent);
        TextView ministry = layout.findViewById(R.id.tvDetailMinistry);
        TextView timeStamp = layout.findViewById(R.id.tvDetailTimeStamp);
        ImageView sourceImage = layout.findViewById(R.id.ivDetailNoteImage);
        Button delete = layout.findViewById(R.id.bDeleteNote);
        Button update = layout.findViewById(R.id.bUpdateNote);


        if (details != null) {

            id = (String)details.get(ApiFields.TAG_ID);

            ministry.setText((String)details.get(ApiFields.TAG_MINISTRY));
            content.setText((String)details.get(ApiFields.TAG_STORIES_CONTENT));
            timeStamp.setText((String)details.get(ApiFields.TAG_STORIES_TIMESTAMP));
            title.setText((String)details.get(ApiFields.TAG_STORIES_TITLE));

            Uri uri = Uri.parse((String)details.get(ApiFields.TAG_STORIES_IMAGE_PATH));
            try {
                Glide.with(this).load(uri).into(sourceImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG,"No map found in parcel");

        }

        //button operations
        delete.setOnClickListener((view) -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Delete")
                            .setMessage("Are you sure you want to delete this story")
                            .setPositiveButton("Yes", (dialog, which) -> delete_note())
                            .setNegativeButton("No", (dialog, which) -> {

                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
        );

        update.setOnClickListener((view) -> {

            //go to previous fragment
            UpdateNote updateNote = new UpdateNote();

            Bundle args = new Bundle();
            args.putParcelable("note_details", new DetailsParcel(details));

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
                /*Show_Notifications show_notifications = new Show_Notifications();
                getFragmentManager().beginTransaction()
                        .replace(R.id.flContent, show_notifications)
                        .addToBackStack(show_notifications.getClass().getSimpleName())
                        .commit();

                getActivity().setTitle("News");*/
                getActivity().onBackPressed();
                Log.d(TAG, "Back Pressed");
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void delete_note() {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE,
                response -> {

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

                        Toast.makeText(getActivity(), "Failed to delete notification, Try Again", Toast.LENGTH_LONG).show();

                    }

                },
                error -> Log.d(TAG, error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);

                return params;
            }
        };

        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

}
