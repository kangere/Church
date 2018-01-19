package material.kangere.com.tandaza;


import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

@Deprecated
public class StaticMethods {

   /* private Toolbar toolbar;
    private DrawerLayout drawerLayout;*/


    public StaticMethods() {

    }



    /**
     * @param a         - AppCompatActivity/ Activity
     * @param toolBarId - id of toolbar to initialise
     * @author Paul Murage
     */
    public static void toolBar(AppCompatActivity a, int toolBarId) {
        Toolbar toolbar = (Toolbar) a.findViewById(toolBarId);
        a.setSupportActionBar(toolbar);
        /*try {
            a.getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*/

        toolbar.setTitleTextColor(Color.WHITE);

        final ActionBar actionBar = a.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
