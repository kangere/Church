package material.kangere.com.tandaza.NavActivities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Button call, email;
    private static String uriString = "tel:0714669642";
    private static String subject = "Information";
    private static String Devsubject = "Contact";

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

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(uri);
                startActivity(callIntent);

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@tandaza.org", null));
                //email.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@tandaza.org"});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                //email.setType(HTTP.PLAIN_TEXT_TYPE);
                startActivity(Intent.createChooser(email, "Send Email"));
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap map) {
        LatLng k3c = new LatLng(-1.277683, 36.788978);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            map.setMyLocationEnabled(true);
            return;
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(k3c, 17));
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
}
