package material.kangere.com.tandaza.NavActivities;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import material.kangere.com.tandaza.NavigationFragment;
import material.kangere.com.tandaza.R;

public class YouthMinistry extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youth_ministry);

        //toolbar initisialization
        toolbar = (Toolbar) findViewById(R.id.youthToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Youth Ministry");
        toolbar.setTitleTextColor(Color.WHITE);

        //drawerlayout init
        drawerLayout = (DrawerLayout) findViewById(R.id.youth_drawer_layout);

        //Navigation fragment init
        NavigationFragment navfrag = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.youth_fragment_navigation_drawer);
        navfrag.setup(drawerLayout,toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_youth_ministry, menu);
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
