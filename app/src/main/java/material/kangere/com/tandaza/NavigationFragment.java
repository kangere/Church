package material.kangere.com.tandaza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import material.kangere.com.tandaza.LocalDB.SQLiteHandler;

/**
 * Created by user on 7/2/2015.
 */
public class NavigationFragment extends Fragment {

    private int[] icons = {R.drawable.ic_home, R.drawable.ic_about, R.drawable.ic_ministries, R.drawable.ic_sermons, R.drawable.ic_connect, R.drawable.ic_event,
            R.drawable.ic_event,R.drawable.ic_give, R.drawable.ic_media, R.drawable.ic_contact};
    private String[] navigationArray;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private TextView tvname,tvemail;
    private SQLiteHandler db;

    public NavigationFragment(){


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //inflate layout for this fragment
        View layout = inflater.inflate(R.layout.navigation_fragment,container,false);
        listView = (ListView) layout.findViewById(R.id.navigation_listview);

        //string array for navigation drawer
        navigationArray = getResources().getStringArray(R.array.navigation);

        //Custom Array Adapter
        NavAdapter navAdapter = new NavAdapter(getActivity(), icons, navigationArray);

        //name email init
        tvname = (TextView)layout.findViewById(R.id.name_nav);
        tvemail = (TextView) layout.findViewById(R.id.email_nav);
        db = new SQLiteHandler(getActivity());
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        tvname.setText(name);
        tvemail.setText(email);



        //attach adapter to listview
        listView.setAdapter(navAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), About.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), Ministries.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), Sermons.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), Connect.class));
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(), UpcomingEvents.class));
                        break;
                    case 6:
                        startActivity(new Intent(getActivity(), PastEvents.class));
                        break;
                    case 7:
                        startActivity(new Intent(getActivity(), Give.class));
                        break;
                    case 8:
                        startActivity(new Intent(getActivity(), Media.class));
                        break;
                    case 9:
                        startActivity(new Intent(getActivity(), Contact.class));
                        break;
                }
            }
        });
        return layout;
    }

    public void setup( DrawerLayout layout,final Toolbar toolbar){
          drawerLayout = layout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {

                mDrawerToggle.syncState();
            }
        });



    }
}
