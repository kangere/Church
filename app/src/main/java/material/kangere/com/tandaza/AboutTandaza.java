package material.kangere.com.tandaza;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by user on 7/23/2015.
 */
public class AboutTandaza extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View layout = inflater.inflate(R.layout.about_fragment,container);
        TextView text = new TextView(container.getContext());
        text.setText("This is in beta testing");
        text.setGravity(Gravity.CENTER);


        return text;
    }
}
