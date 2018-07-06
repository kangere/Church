package material.kangere.com.tandaza.NavActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import java.sql.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.ApiFields;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.RequestQueueSingleton;

public class ViewEvent extends AppCompatActivity {

    private String TAG = ViewEvent.class.getSimpleName();

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        setTitle("Event");

        Toolbar toolbar = findViewById(R.id.tbEvent);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));

        //enable up navigation
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        String [] single_event = bundle.getStringArray("single_event");

        TextView title = findViewById(R.id.tvViewEventsTitle);
        ImageView poster = findViewById(R.id.ivViewEventImage);
        TextView date = findViewById(R.id.tvEventDate);
        TextView time = findViewById(R.id.tvEventTime);
        TextView Ministry = findViewById(R.id.tvEventMinistry);
        TextView Venue = findViewById(R.id.tvEventVenue);
        TextView Description = findViewById(R.id.tvViewEventDescription);

        if(single_event != null)
        {
            id = single_event[7];
            Date date1 = Date.valueOf(single_event[2]);
            String formatted_date = DateFormat.getDateInstance().format(date1);

            title.setText(single_event[0]);
            date.setText(formatted_date);
            time.setText(single_event[3]);
            Ministry.setText(single_event[4]);
            Venue.setText(single_event[5]);
            Description.setText(single_event[6]);

            Uri uri = Uri.parse(single_event[1]);

            Glide.with(this).load(uri).into(poster);

//            Log.d(TAG,formatted_date);
        }
        else
        {
            Log.d(TAG,"No event passed");
        }

        Button edit = findViewById(R.id.bEditEvent);

        edit.setOnClickListener(
                view -> {

                    Intent intent = new Intent(this,UpdateEvent.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
        );

        Button delete = findViewById(R.id.bDeleteEvent);

        delete.setOnClickListener(

                view -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Delete")
                            .setMessage("Are you sure you want to delete this event?")
                            .setPositiveButton("Yes", (dialog,which) -> delete_event())
                            .setNegativeButton("No", (dialog,which) -> {

                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete_event()
    {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE_EVENT,
                response -> {

                    Log.d(TAG,response);

                    int success = 0;

                    try{
                        JSONObject object = new JSONObject(response);
                        success = object.getInt(ApiFields.TAG_SUCCESS);

                    }catch (JSONException e) {
                        Log.d(TAG,e.getMessage() );
                    }

                    if(success == 1)
                    {
                        finish();
                    }
                    else
                    {
                        Toast.makeText(this,"Unable to delete notification",Toast.LENGTH_LONG).show();
                    }
                },
                error -> Log.d(TAG,error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String,String> param = new HashMap<>();
                param.put(ApiFields.TAG_EVENT_ID,id);

                return param;
            }
        };

        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
    }
}
