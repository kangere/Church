package material.kangere.com.tandaza;

import android.content.Intent;
import android.graphics.Color;
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

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    MinistryRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ministries);
        toolbar = (Toolbar) findViewById(R.id.ministriesToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ministries");
        toolbar.setTitleTextColor(Color.WHITE);

        //drawer layout init
        drawerLayout = (DrawerLayout) findViewById(R.id.ministries_drawer_layout);


        //navigation fragment init
        NavigationFragment navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.ministries_fragment_navigation_drawer);
        navigationFragment.setup(drawerLayout, toolbar);

        //recyclerView initialisation
        recyclerView = (RecyclerView) findViewById(R.id.min_recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);


        //recyclerview adapter initialisation
        adapter = new MinistryRecyclerAdapter(this,getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void itemClicked(View v,int position){
        switch(position){
            case 0:
                startActivity(new Intent(this,YouthMinistry.class));
                break;
            case 1:
                startActivity(new Intent(this, ChildrenMinistry.class));
                break;
            case 2:
                Toast.makeText(this,"Chosen " + position,Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(this,"Chosen " + position,Toast.LENGTH_LONG).show();
                break;
            
        }

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

    public List<MinInfo> getData(){

        List<MinInfo> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_about,R.drawable.children_ministry,R.drawable.ic_about,R.drawable.kings_men};
        String[] titles = {"Youth Ministry","Children Ministry","Sarah's Treasure","Kingsmen"};
        for (int i = 0; i < 4; i++){
            MinInfo current = new MinInfo();
            current.iconId = icons[i];
            current.title = titles[i];

            data.add(current);

        }
        return data;


    }
}
