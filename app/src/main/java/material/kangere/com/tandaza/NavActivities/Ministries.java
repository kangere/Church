package material.kangere.com.tandaza.NavActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import material.kangere.com.tandaza.Adapters.MinAdapter;
import material.kangere.com.tandaza.MinInfo;
import material.kangere.com.tandaza.MinistryRecyclerAdapter;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.StaticMethods;


public class Ministries extends AppCompatActivity  {


    private ListView listView;
    private RecyclerView recyclerView;
    private MinistryRecyclerAdapter adapter;
    ArrayList<MinInfo> data = new ArrayList<>();

    private int[] images = {R.mipmap.youth, R.mipmap.ark, R.mipmap.sarahs, R.mipmap.kings_men, R.mipmap.ndoa
            , R.mipmap.bofu};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ministries);
        //Nav and toolbar initialisation
        StaticMethods.ClassInitisialisation(this, R.id.ministries_fragment_navigation_drawer, R.id.ministriesToolbar, R.id.ministries_drawer_layout);


        String[] titles = getResources().getStringArray(R.array.ministry_header);
        //List view initialisation
        listView = (ListView) findViewById(R.id.lvMin);
        MinAdapter adapter = new MinAdapter(this,titles,images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        startActivity(new Intent(Ministries.this,YouthMinistry.class));
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
            }
        });
        //recyclerView initialisation
       /*recyclerView = (RecyclerView) findViewById(R.id.min_recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);*/


        //recyclerview adapter initialisation


       /*try {
            adapter = new MinistryRecyclerAdapter(getBaseContext());
            adapter.setClickListener(this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter);
        getData();*/

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


    public void getData() {
        int[] icons = {R.drawable.ic_about, R.drawable.children_ministry, R.drawable.ic_about, R.drawable.kings_men};
        String[] titles = {"Youth Ministry", "Children Ministry", "Sarah's Treasure", "Kingsmen"};


        for (int i = 0; i < titles.length; i++) {
            MinInfo current = new MinInfo();
            current.setIconId(icons[i]);
            current.setTitle(titles[i]);

            data.add(current);

        }
        adapter.setMinistries(data);
        //return data;


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
