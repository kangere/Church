package material.kangere.com.tandaza.NavActivities;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.widget.Toolbar;

import material.kangere.com.tandaza.NavigationFragment;
import material.kangere.com.tandaza.R;


public class Give extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give);

        //toolbar init
        toolbar = (Toolbar) findViewById(R.id.giveToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Give");
        toolbar.setTitleTextColor(Color.WHITE);

        //drawer layout init
        drawerLayout = (DrawerLayout) findViewById(R.id.give_drawer_layout);


        //navigation fragment init
        NavigationFragment navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.give_fragment_navigation_drawer);
        navigationFragment.setup(drawerLayout, toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_give, menu);
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
}
