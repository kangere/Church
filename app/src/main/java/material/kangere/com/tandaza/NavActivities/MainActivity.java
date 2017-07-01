package material.kangere.com.tandaza.NavActivities;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import material.kangere.com.tandaza.LocalDB.SQLiteHandler;
import material.kangere.com.tandaza.LocalDB.TablesContract;
import material.kangere.com.tandaza.R;


public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawer;
    private NavigationView navigationView;

    private SQLiteHandler db;


    Toolbar toolbar;
    // boolean isIntentSafe;
    //Intent facebookAppIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.flContent, new MainContent()).commit();

        }

        setupNavView();

        //toolbar initialisation
        toolbar = (Toolbar) findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //create cache tables in local database
        db.addTable(TablesContract.EventsEntry.TABLE_NAME, TablesContract.EventsEntry.COLUMN_EVENT_CACHE);
        db.addTable(TablesContract.NotificationsCache.TABLE_NAME, TablesContract.NotificationsCache.COLUMN_NOTE_CACHE);


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
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
        if (id == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavView() {

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getTitle().toString()) {
                    case "Home":
                        // Create new fragment
                        MainContent main = new MainContent();
                        openFragment(main, menuItem);
                        break;

                    case "News":
                        // Create new fragment
                        Show_Notifications show_notifications = new Show_Notifications();
                        //call openFragment method
                        openFragment(show_notifications, menuItem);
                        break;

                    case "Events":
                        //Create new fragment
                        UpcomingEvents upcomingEvents = new UpcomingEvents();
                        //call openFragment method
                        openFragment(upcomingEvents, menuItem);
                        break;

                    case "Contact":
                        //create new contact fragment
                        Contact contact = new Contact();
                        //call openFragment method to perform fragment transaction
                        openFragment(contact, menuItem);

                        break;

                    case "Ministries":
                        Ministries ministries = new Ministries();
                        //call openfrag method
                        openFragment(ministries, menuItem);
                        //startActivity(new Intent(MainActivity.this,Ministries.class));
                        break;

                    case "T-Groups":
                        T_Group t_group = new T_Group();
                        //call openfrag method
                        openFragment(t_group, menuItem);

                        break;

                }
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Home");
    }

    private void openFragment(Fragment fragment, MenuItem menuItem) {
        getFragmentManager().beginTransaction()
                .replace(R.id.flContent, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();

        setTitle(menuItem.getTitle());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //setTitle();
    }
}
