package material.kangere.com.tandaza;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Ministries extends AppCompatActivity implements MinistryRecyclerAdapter.ClickListener {


    private RecyclerView recyclerView;
    private MinistryRecyclerAdapter adapter;
    ArrayList<MinInfo> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ministries);
        //Nav and toolbar initialisation
        InitToolbar.ClassInitisialisation(this, R.id.ministries_fragment_navigation_drawer, R.id.ministriesToolbar, R.id.ministries_drawer_layout);


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

    @Override
    public void itemClicked(View v,int position){
        switch(position){
            case 0:
                startClass(YouthMinistry.class);
                break;
            case 1:
                startClass(ChildrenMinistry.class);
                break;
            case 2:
                startClass(SarahsTreasure.class);
                break;
            case 3:
                startClass(KingsMen.class);
                break;
            
        }

    }

    //method to start class from selected card, takes in the class name as parameter
    public void startClass(Class classes){
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



    public void getData(){
        int[] icons = {R.drawable.ic_about,R.drawable.children_ministry,R.drawable.ic_about,R.drawable.kings_men};
        String[] titles = {"Youth Ministry","Children Ministry","Sarah's Treasure","Kingsmen"};


        for (int i = 0; i < titles.length; i++){
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
