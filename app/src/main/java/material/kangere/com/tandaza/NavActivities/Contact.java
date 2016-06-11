package material.kangere.com.tandaza.NavActivities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import material.kangere.com.tandaza.NavigationFragment;
import material.kangere.com.tandaza.R;


public class Contact extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = Contact.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private int checkPermission;
    private Button call, email;
    private static String uriString = "tel:0714669642";
    private static String subject = "Information";
    private static String Devsubject = "Contact";
    private boolean services = false;
    private static final int MY_PERMISSION_REQUEST_lOCATION = 22;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //UI Initialisation
        call = (Button) findViewById(R.id.bCall);
        email = (Button) findViewById(R.id.bEmail);
        final Uri uri = Uri.parse(uriString);

        //toolbar init
        toolbar = (Toolbar) findViewById(R.id.contactToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Contact");
        toolbar.setTitleTextColor(Color.WHITE);

        //drawer layout init
        drawerLayout = (DrawerLayout) findViewById(R.id.contact_drawer_layout);


        //navigation fragment init
        NavigationFragment navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.contact_fragment_navigation_drawer);
        navigationFragment.setup(drawerLayout, toolbar);
        CheckGooglePlayServices();

        //store permision
        checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //check if permission is granted
        if(checkPermission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_lOCATION);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //sends user to call application with explicit intent
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(uri);
                startActivity(callIntent);

            }
        });

        //send user to email client with explicit intent
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@tandaza.org", null));
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(email, "Send Email"));
            }
        });
    }


    //handle user permission response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode)
        {
            case MY_PERMISSION_REQUEST_lOCATION:

                if(grantResults.length > 0 && grantResults[0] == MY_PERMISSION_REQUEST_lOCATION)
                {
                    //if permission is granted
                    //code dependant on permission to follow
                        map.setMyLocationEnabled(true);

                }
                else
                {
                    //permission is denied
                    map.setMyLocationEnabled(false);
                }

                return;

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //input k3c specific latitude and longitude
        LatLng k3c = new LatLng(-1.277683, 36.788978);

        //place map location to specific latitude and longitude locations
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(k3c, 17));

        //add marker to the location
        map.addMarker(new MarkerOptions()
                .position(k3c)
                .title("Kileleshwa Covenant Community Church")
                .snippet("Gichugu Road, Kileleshwa – Nairobi Kenya"));
    }

    public void EmailDeveloper(View view) {
        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "kangere721@gmail.com", null));
        email.putExtra(Intent.EXTRA_SUBJECT, Devsubject);
        startActivity(Intent.createChooser(email, "Send Email"));
    }

    public void CheckGooglePlayServices(){

        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.gms", 0);
            services = true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            services = false;
        }

        if(services){
            Log.d(TAG,"Google play services available and working");
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Contact.this);

            // set dialog message
            alertDialogBuilder
                    .setTitle("Google Play Services")
                    .setMessage("The map requires Google Play Services to be installed.")
                    .setCancelable(true)
                    .setPositiveButton("Install", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss();
                            // Try the new HTTP method (I assume that is the official way now given that google uses it).
                            try
                            {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                intent.setPackage("com.android.vending");
                                startActivity(intent);
                            }
                            catch (ActivityNotFoundException e)
                            {
                                // Ok that didn't work, try the market method.
                                try
                                {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    intent.setPackage("com.android.vending");
                                    startActivity(intent);
                                }
                                catch (ActivityNotFoundException f)
                                {
                                    // Ok, weird. Maybe they don't have any market app. Just show the website.

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    startActivity(intent);
                                }
                            }
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    }
}
