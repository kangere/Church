package material.kangere.com.tandaza.NavActivities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import material.kangere.com.tandaza.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetails extends Fragment {


    public EventDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_view,container,false);

        TextView title = view.findViewById(R.id.tv_event_title);

        TextView time = view.findViewById(R.id.tv_event_time);
        TextView date = view.findViewById(R.id.tv_event_date);
        TextView loaction = view.findViewById(R.id.tv_event_location);
        TextView description = view.findViewById(R.id.tv_event_description);



        return view;
    }

}
