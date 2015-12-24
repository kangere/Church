package material.kangere.com.tandaza.NavActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import material.kangere.com.tandaza.Adapters.EventAdapter;
import material.kangere.com.tandaza.CheckNetwork;
import material.kangere.com.tandaza.R;


public class UpcomingEvents extends AppCompatActivity implements EventAdapter.EventsClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;

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
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.rvEvents);
        EventAdapter adapter = new EventAdapter(getBaseContext());
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        if(CheckNetwork.isInternetAvailable(UpcomingEvents.this)){


        }else{
            Snackbar.make(coordinatorLayout,"No Internet Connection",Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            overridePendingTransition( 0, 0);
                            startActivity(getIntent());
                            overridePendingTransition( 0, 0);
                        }
                    }).show();

        }


    }

    @Override
    public void itemClicked(View view, int position) {

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


        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public void CreateEvent(View view){
        startActivity(new Intent(this,Create_Event.class));
    }
}
