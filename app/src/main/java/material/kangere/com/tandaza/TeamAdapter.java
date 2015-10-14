package material.kangere.com.tandaza;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by user on 9/16/2015.
 */
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private Context c;
    private LayoutInflater inflate;
    List<TeamInfo> list = Collections.emptyList();
    public TeamAdapter(Context c,List<TeamInfo> list){
        this.c = c;
        inflate = LayoutInflater.from(c);
        this.list = list;



    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflate.inflate(R.layout.team_cardview,parent,false);
        TeamViewHolder team = new TeamViewHolder(view);
        return team;
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
     TeamInfo teamInfo = list.get(position);

        holder.name.setText(teamInfo.getName());
        holder.image.setImageResource(teamInfo.getImage());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView image;
        public TeamViewHolder(View v){
            super(v);
            name =(TextView) v.findViewById(R.id.tvTeam);
            image = (ImageView) v.findViewById(R.id.ivTeam);


        }

    }
}
