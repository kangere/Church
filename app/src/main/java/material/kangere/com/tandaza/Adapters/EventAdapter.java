package material.kangere.com.tandaza.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import material.kangere.com.tandaza.EventData;
import material.kangere.com.tandaza.R;

/**
 * Created by user on 12/17/2015.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvents> {

    private final String TAG = EventAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<EventData> eventsList = new ArrayList<>();
    private LayoutInflater inflater;
    private EventsClickListener clickListener;

    public EventAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setEventsList(ArrayList<EventData> list){
        this.eventsList = list;
        notifyItemRangeChanged(0, list.size());
    }
    public void setClickListener(EventsClickListener eventsClickListener){
        this.clickListener = eventsClickListener;
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    @Override
    public ViewHolderEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_card,parent,false);
        //ViewHolderEvents viewHolderEvents = new ViewHolderEvents(view);
        return new ViewHolderEvents(view);
    }
    @Override
    public void onBindViewHolder(ViewHolderEvents holder, int position) {
        EventData current = eventsList.get(position);
//        Log.d(TAG,current.getPosterpath());

        Uri uri = Uri.parse(current.getPosterpath());

        //load image to imageview with glide library
        Glide.with(context).load(uri).into(holder.poster);
        holder.imgpathgone.setText(current.getPosterpath());

        //set text to appropriate textview for the event card
        holder.id.setText(current.getId());
        holder.title.setText(current.getTitle());
        holder.date.setText(current.getDate());
        holder.time.setText(current.getTime());
        holder.venue.setText(current.getVenue());
        holder.description.setText(current.getDescription());
        holder.ministry.setText(current.getMinistry());



    }

    class ViewHolderEvents extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView id,title,date,ministry,venue,time,description,imgpathgone;
        private ImageView poster;
        public ViewHolderEvents(View view){
            super(view);

            id = view.findViewById(R.id.tvGoneEventId);
            ministry = view.findViewById(R.id.tvGoneEventMinistry);
            date =  view.findViewById(R.id.tvGoneEventDate);
            venue =  view.findViewById(R.id.tvGoneEventVenue);
            time =  view.findViewById(R.id.tvGoneEventTime);
            imgpathgone = view.findViewById(R.id.tvGoneEventImgpath);

            title =  view.findViewById(R.id.tvEventsTitle);
            description =  view.findViewById(R.id.tvEventDescription);
            poster = view.findViewById(R.id.ivEventImage);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.itemClicked(v,getAdapterPosition());
            }
        }

    }
    public interface EventsClickListener{
        void itemClicked(View view,int position);
    }

}
