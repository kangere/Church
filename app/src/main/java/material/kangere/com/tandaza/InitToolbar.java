package material.kangere.com.tandaza;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;


public class InitToolbar {

   /* private Toolbar toolbar;
    private DrawerLayout drawerLayout;*/


    public InitToolbar(){

    }

    /**
     * @author Paul Murage
     * @param navigation_fragment_id - id of navigation bar to initialise
     * @param toolbarID - id of toolbar to initialise
     * @param drawerlayout_id - id of drawerlayout  to initialise
     */
    public  static void ClassInitisialisation(AppCompatActivity a,int navigation_fragment_id,int toolbarID,int drawerlayout_id){
        Toolbar  toolbar = (Toolbar) a.findViewById(toolbarID);
        a.setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        //drawerlayout init
        DrawerLayout drawerLayout = (DrawerLayout) a.findViewById(drawerlayout_id);

        try {
            //Navigation fragment init
            NavigationFragment navfrag = (NavigationFragment) a.getSupportFragmentManager().findFragmentById(navigation_fragment_id);
            navfrag.setup(drawerLayout, toolbar);
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(RuntimeException e){
            e.printStackTrace();
        }


    }
}
