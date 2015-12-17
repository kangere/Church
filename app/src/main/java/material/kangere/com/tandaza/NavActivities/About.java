package material.kangere.com.tandaza.NavActivities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import material.kangere.com.tandaza.AboutTandaza;
import material.kangere.com.tandaza.InitToolbar;
import material.kangere.com.tandaza.R;


public class About extends AppCompatActivity implements MaterialTabListener {


    private MaterialTabHost materialTabHost;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private String[] aboutArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);


        //toolbar initialization
        InitToolbar.ClassInitisialisation(this, R.id.about_fragment_navigation_drawer, R.id.aboutToolbar, R.id.about_drawer_layout);

        //array for titles
        aboutArray = getResources().getStringArray(R.array.About);

        //tabs initialization
        pager = (ViewPager) findViewById(R.id.aboutviewPager);
        /*PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);*/
        materialTabHost = (MaterialTabHost) findViewById(R.id.aboutTabHost);


        //init view pager
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                materialTabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //insert all tabs from pageradapter data

        for (int i = 0; i < adapter.getCount(); i++) {
            materialTabHost.addTab(
                    materialTabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }


        /*SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.aboutTabs);
        viewPagerTab.setViewPager(pager);*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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

    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {

            switch (num) {
                case 0:
                    Log.d("About", " populates about fragment");
                    // About fragment
                    AboutTandaza ab = new AboutTandaza();
                    return ab;
                case 1:
                    // Games fragment activity
                    return new Vision();
                case 2:
                    // Movies fragment activity
                    return new Team();
            }

            return null;


        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return aboutArray[position];
        }

    }
}

