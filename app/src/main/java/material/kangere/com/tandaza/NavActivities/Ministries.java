package material.kangere.com.tandaza.NavActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import material.kangere.com.tandaza.MinData;
import material.kangere.com.tandaza.MinistryRecyclerAdapter;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.StaticMethods;


public class Ministries extends AppCompatActivity implements MinistryRecyclerAdapter.ClickListener {


    private ListView listView;
    private RecyclerView recyclerView;
    private MinistryRecyclerAdapter adapter;
    List<MinData> data = new ArrayList<>();
    private String[] titles;
    private String[] info;

    //private int[] images = {R.mipmap.youth, R.mipmap.ark, R.mipmap.sarahs, R.mipmap.kings_men, R.mipmap.ndoa
    //, R.mipmap.bofu};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ministries);
        //Nav and toolbar initialisation
        StaticMethods.ClassInitisialisation(this, R.id.ministries_fragment_navigation_drawer, R.id.ministriesToolbar, R.id.ministries_drawer_layout);
        titles = getResources().getStringArray(R.array.ministry_header);
        info = getResources().getStringArray(R.array.minitry_text);
        //List view initialisation


        //recyclerView initialisation
        recyclerView = (RecyclerView) findViewById(R.id.min_recycler_view);
        recyclerView.setHasFixedSize(true);
        MinistryRecyclerAdapter adapter = new MinistryRecyclerAdapter(this, getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    public void itemClicked(View v, int position) {

        switch (position) {
            case 0:

                startClass(YouthMinistry.class);
                break;
            case 1:

                startClass(ChildrenMinistry.class);
                break;


        }
    }

    //method to start class from selected card, takes in the class name as parameter
    public void startClass(Class classes) {
        startActivity(new Intent(this, classes));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ministries, menu);
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

        return super.onOptionsItemSelected(item);
    }


    public List<MinData> getData() {


        for (int i = 0; i < titles.length; i++) {
            MinData current = new MinData();
            current.setInfo(info[i]);
            current.setTitle(titles[i]);

            data.add(current);

        }

        return data;


    }

    @Override
    protected void onStop() {
        super.onStop();
        // data.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getData();
    }
}
