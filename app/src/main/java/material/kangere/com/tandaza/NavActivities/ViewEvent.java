package material.kangere.com.tandaza.NavActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.sql.Date;
import java.text.DateFormat;

import material.kangere.com.tandaza.R;

public class ViewEvent extends AppCompatActivity {

    private String TAG = ViewEvent.class.getSimpleName();
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
}
