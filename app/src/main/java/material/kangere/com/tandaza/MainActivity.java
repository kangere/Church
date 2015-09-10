package material.kangere.com.tandaza;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private int[] icons = {R.drawable.ic_home, R.drawable.ic_about, R.drawable.ic_ministries, R.drawable.ic_sermons, R.drawable.ic_connect, R.drawable.ic_event,
            R.drawable.ic_event,R.drawable.ic_give, R.drawable.ic_media, R.drawable.ic_contact};
    private String[] mNav;
    private DrawerLayout mDrawer;
    private ListView mList;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar init
        toolbar = (Toolbar) findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        mNav = getResources().getStringArray(R.array.navigation);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //navigation fragment init
        NavigationFragment navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navigationFragment.setup(mDrawer,toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
