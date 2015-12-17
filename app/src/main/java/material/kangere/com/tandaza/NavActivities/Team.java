package material.kangere.com.tandaza.NavActivities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.TeamAdapter;
import material.kangere.com.tandaza.TeamInfo;


public class Team extends Fragment {

    private RecyclerView rv;
    TeamAdapter tm;
    int[] icons = {R.drawable.ken_kamau,R.drawable.david_obuki,R.drawable.peter_mwangi,R.drawable.emmah_nungari,R.drawable.esther_opiyo,
            R.drawable.jackson_kamau,R.drawable.arnold_omondi,R.drawable.yvonne_karongo,R.drawable.peter_mwanzia,R.drawable.beryl_karabilo,
            R.drawable.mirriam_wangeci,R.drawable.moses_musau,R.drawable.linda_waweru,R.drawable.grace_makandi,R.drawable.carol_muthoni,
            R.drawable.mary_minayo,R.drawable.denis_ochieng,R.drawable.monica_minani,R.drawable.paul_juma,R.drawable.sam_kimiti,R.drawable.wacuka_kamau
            ,R.drawable.john_ndubai};
    String[] titles;
    List<TeamInfo> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.team,null);

        rv = (RecyclerView) v.findViewById(R.id.team_recycler_view);
        rv.setHasFixedSize(true);
        StaggeredGridLayoutManager sgLm = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(sgLm);

        tm = new TeamAdapter(getActivity(),getData());
        rv.setAdapter(tm);


        return v;
    }

    public List<TeamInfo> getData(){

        //List<TeamInfo> data = new ArrayList<>();

         titles = getResources().getStringArray(R.array.team);
        for (int i = 0; i < titles.length; i++){
            TeamInfo current = new TeamInfo();
            current.setImage(icons[i]) ;
            current.setName(titles[i]) ;

            data.add(current);

        }
        //new LoadinBack().execute();
        return data;


    }
    private class LoadinBack extends AsyncTask<Void,Void,List<TeamInfo>>{

        @Override
        protected List<TeamInfo> doInBackground(Void... params) {
            TeamInfo current = new TeamInfo();
            for (int i = 0; i < titles.length; i++){

                current.setImage(icons[i]) ;
                current.setName(titles[i]) ;

                data.add(current);

            }
            return data;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        data.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
