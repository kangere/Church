package material.kangere.com.tandaza.NavActivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import material.kangere.com.tandaza.R;


public class Vision extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View Layout = inflater.inflate(R.layout.vision,null);
        return Layout;
    }
}
