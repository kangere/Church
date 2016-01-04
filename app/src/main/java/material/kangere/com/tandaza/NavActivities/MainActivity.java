package material.kangere.com.tandaza.NavActivities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import material.kangere.com.tandaza.LocalDB.SQLiteHandler;
import material.kangere.com.tandaza.LocalDB.TablesContract.EventsEntry;
import material.kangere.com.tandaza.LocalDB.TablesContract.NotificationsCache;
import material.kangere.com.tandaza.Login;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.SessionManager;
import material.kangere.com.tandaza.StaticMethods;


public class MainActivity extends ActionBarActivity {


    private String[] mNav;
    private DrawerLayout mDrawer;
    private ListView mList;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    private Button btnLogout;
    private TextView txtName, txtEmail;
    private SQLiteHandler db;
    private SessionManager session;
    public static  String facebookID = "129991457013820";
    public static String FACEBOOK_URL = "https://www.facebook.com/K3C.Tandaza";
    boolean isIntentSafe;
    Intent facebookAppIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar init and nav frag
        StaticMethods.ClassInitisialisation(this, R.id.fragment_navigation_drawer, R.id.toolBarMain, R.id.drawer_layout);

       /* txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);*/

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM " + NotificationsCache.TABLE_NAME;
        String eventQuery = "SELECT * FROM " + EventsEntry.TABLE_NAME;
        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            db.addNotificationCache();
        }
        Cursor cursor = database.rawQuery(eventQuery,null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0)
                db.addEventCache();


        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
       /* Intent i = getIntent();
        String emailIntent = i.getStringExtra("email");
        HashMap<String, String> user = db.getUserDetails(emailIntent);

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);*/

        // Logout button click event
       /* btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });*/
        /*final String url = "fb://page/" + facebookID;
        facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(facebookAppIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
         isIntentSafe = activities.size() > 0;*/
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

    private void logoutUser() {
        session.setLogin(false);

        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
    public void OpenFbPage(View view){
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(this);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + facebookID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
