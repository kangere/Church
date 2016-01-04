package material.kangere.com.tandaza;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 8/3/2015.
 */
public class MinistryRecyclerAdapter extends RecyclerView.Adapter<MinistryRecyclerAdapter.MinistryViewHolder> {

    private final String TAG = "Recycler";
    //private ClickListener clickListener;
    private Context context;
    private LayoutInflater inflater;
   List<MinData> data = Collections.emptyList();
    public ClickListener clickListener;

    public MinistryRecyclerAdapter(Context context,List<MinData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;


    }


    @Override
    public MinistryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder called");
        View view = inflater.inflate(R.layout.ministry_cardview, viewGroup, false);
        MinistryViewHolder holder = new MinistryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MinistryViewHolder ministryViewHolder, int i) {
        MinData current = data.get(i);
        Log.d(TAG,"onBindViewHolder called");
        if(i == 0){
            ministryViewHolder.description.setMaxLines(5);
        }
        ministryViewHolder.description.setText(current.getInfo());
        ministryViewHolder.header.setText(current.getTitle());
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ClickListener{
        public void itemClicked(View v, int position);

    }



     class MinistryViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView header,description;

        public MinistryViewHolder (View v){
            super(v);
            description = (TextView) v.findViewById(R.id.tvMinText);
            header = (TextView) v.findViewById(R.id.tvMinTitle);
            v.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
           if(clickListener != null){
               clickListener.itemClicked(v,getAdapterPosition());

           }

        }
    }



    /**
     * Created by user on 8/3/2015.
     */



}



