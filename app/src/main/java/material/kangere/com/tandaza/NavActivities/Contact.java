package material.kangere.com.tandaza.NavActivities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import material.kangere.com.tandaza.R;


public class Contact extends Fragment implements OnMapReadyCallback {

    private static final String TAG = Contact.class.getSimpleName();
    private static final String T_GROUPLINK = "http://tandaza.org/connect/";
    private static final String SERVELINK = "http://tandaza.org/signup-to-serve/";

    private int checkPermission;
    private Button call, email , emailDev , t_group, serve;
    private static String uriString = "tel:0714669642";
    private static String subject = "Information";
    private static String Devsubject = "Contact";
    private boolean services = false;
    private static final int MY_PERMISSION_REQUEST_lOCATION = 22;
    GoogleMap map;
    SupportMapFragment supportMapFragment;
    MapView mapView;
    private Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact, container, false);

        //UI Initialisation
        call = (Button) view.findViewById(R.id.bCall);
        email = (Button) view.findViewById(R.id.bEmail);
        emailDev = (Button) view.findViewById(R.id.bEmailDev);
        t_group = (Button) view.findViewById(R.id.bfindTgroup);
        serve = (Button) view.findViewById(R.id.bServe);
        final Uri uri = Uri.parse(uriString);

        CheckGooglePlayServices();

        //store permision
        checkPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        //check if permission is granted
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_lOCATION);
        }

        mapView =(MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        try{
            mapView.getMapAsync(this);
        }catch(NullPointerException e)
        {
            Log.d(TAG," " + e);
        }




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

        //send user to email client with explicit intent
        emailDev.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "kangere721@gmail.com", null));
                email.putExtra(Intent.EXTRA_SUBJECT, Devsubject);
                startActivity(Intent.createChooser(email, "Send Email"));
            }
        });

        t_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(T_GROUPLINK));
                startActivity(browserIntent);
            }
        });
        serve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SERVELINK));
                startActivity(browserIntent);
            }
        });
        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
    }

    //handle user permission response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_lOCATION:

                if (grantResults.length > 0 && grantResults[0] == MY_PERMISSION_REQUEST_lOCATION) {
                    //if permission is granted
                    //code dependant on permission to follow
                    if(checkPermission !=  PackageManager.PERMISSION_GRANTED)
                    map.setMyLocationEnabled(true);

                } else {
                    //permission is denied
                    map.setMyLocationEnabled(false);
                }

                return;

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //place map location to specific latitude and longitude locations
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-1.277683, 36.788978), 17));

        //add marker to the location
        map.addMarker(new MarkerOptions()
                .position(new LatLng(-1.277683, 36.788978))
                .title("Kileleshwa Covenant Community Church")
                .snippet("Gichugu Road, Kileleshwa – Nairobi Kenya"));
    }

    public void emailDeveloper(View view) {
        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "kangere721@gmail.com", null));
        email.putExtra(Intent.EXTRA_SUBJECT, Devsubject);
        startActivity(Intent.createChooser(email, "Send Email"));
    }

    public void CheckGooglePlayServices() {

        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.gms", 0);
            services = true;
        } catch (PackageManager.NameNotFoundException e) {
            services = false;
        }

        if (services) {
            Log.d(TAG, "Google play services available and working");
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set dialog message
            alertDialogBuilder
                    .setTitle("Google Play Services")
                    .setMessage("The map requires Google Play Services to be installed.")
                    .setCancelable(true)
                    .setPositiveButton("Install", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            // Try the new HTTP method (I assume that is the official way now given that google uses it).
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                intent.setPackage("com.android.vending");
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // Ok that didn't work, try the market method.
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    intent.setPackage("com.android.vending");
                                    startActivity(intent);
                                } catch (ActivityNotFoundException f) {
                                    // Ok, weird. Maybe they don't have any market app. Just show the website.

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    startActivity(intent);
                                }
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        getActivity().setTitle("Contact");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
