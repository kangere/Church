package material.kangere.com.tandaza.NavActivities;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import material.kangere.com.tandaza.R;

/**
 * Created by user on 13-Jun-16.
 */
public class MainContent extends Fragment {


    private static  String facebookID = "129991457013820";
    private static String FACEBOOK_URL = "https://www.facebook.com/K3C.Tandaza";
    private View rootView;

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fb:
                    OpenFbPage();
                    break;
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View Layout = inflater.inflate(R.layout.fragment_content_main,container,false);

        rootView =  Layout.findViewById(R.id.fb);
        rootView.setOnClickListener(mOnClickListener);

        return Layout;
    }

    public void OpenFbPage(){
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(getActivity());
        facebookIntent.setData(Uri.parse(facebookUrl));


        if(facebookIntent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(facebookIntent);
        else
            Toast.makeText(getActivity(),"Could not find app to open facebook",Toast.LENGTH_LONG).show();

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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Home");
    }
}
