package material.kangere.com.tandaza.NavActivities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import material.kangere.com.tandaza.MinData;
import material.kangere.com.tandaza.MinistryRecyclerAdapter;
import material.kangere.com.tandaza.R;


public class Ministries extends Fragment implements MinistryRecyclerAdapter.ClickListener {


    private ListView listView;
    private RecyclerView recyclerView;
    private MinistryRecyclerAdapter adapter;
    List<MinData> data = new ArrayList<>();
    private String[] titles;
    private String[] info;

    private int[] images = {R.drawable.youth, R.mipmap.ark, R.mipmap.sarahs, R.mipmap.kings_men, R.mipmap.ndoa
            , R.mipmap.bofu};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //Nav and toolbar initialisation
//        StaticMethods.ClassInitisialisation(this, R.id.ministries_fragment_navigation_drawer, R.id.ministriesToolbar, R.id.ministries_drawer_layout);
        titles = getResources().getStringArray(R.array.ministry_header);
        info = getResources().getStringArray(R.array.minitry_text);

//        //List view initialisation
//
//

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ministries, container, false);

        //recyclerView initialisation
        recyclerView = (RecyclerView) view.findViewById(R.id.min_recycler_view);
        recyclerView.setHasFixedSize(true);
        MinistryRecyclerAdapter adapter = new MinistryRecyclerAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void itemClicked(View v, int position) {

        switch (position) {
            case 0:

                startClass(YouthMinistry.class);
                break;
            case 1:

                startClass(ChildrenMinistry.class);
                break;


        }
    }

    //method to start class from selected card, takes in the class name as parameter
    public void startClass(Class classes) {
        startActivity(new Intent(getActivity(), classes));
    }


    public List<MinData> getData() {


        for (int i = 0; i < titles.length; i++) {
            MinData current = new MinData();
            current.setInfo(info[i]);
            current.setTitle(titles[i]);
            current.setImage(images[i]);

            data.add(current);

        }

        return data;


    }

    @Override
    public void onStop() {
        super.onStop();
        // data.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Ministries");
    }
}
