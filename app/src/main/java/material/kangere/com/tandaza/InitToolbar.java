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
    private AppCompatActivity a;

    public InitToolbar(AppCompatActivity a){
        this.a = a;
    }
    public void ClassInitisialisation(int navigation_fragment_id,int toolbarID,int drawerlayout_id){
        Toolbar  toolbar = (Toolbar) a.findViewById(toolbarID);
        a.setSupportActionBar(toolbar);
        toolbar.setTitle("Youth Ministry");
        toolbar.setTitleTextColor(Color.WHITE);

        //drawerlayout init
        DrawerLayout drawerLayout = (DrawerLayout) a.findViewById(drawerlayout_id);

        //Navigation fragment init
        NavigationFragment navfrag = (NavigationFragment) a.getSupportFragmentManager().findFragmentById(navigation_fragment_id);
        navfrag.setup(drawerLayout,toolbar);


    }
}
