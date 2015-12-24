package material.kangere.com.tandaza.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import material.kangere.com.tandaza.EventData;
import material.kangere.com.tandaza.R;

/**
 * Created by user on 12/17/2015.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvents> {

    private Context context;
    private ArrayList<EventData> eventsList = new ArrayList<>();
    private LayoutInflater inflater;
    private EventsClickListener clickListener;

    public EventAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void onBindViewHolder(ViewHolderEvents holder, int position) {
        EventData current = eventsList.get(position);

        holder.title.setText(current.getTitle());
        holder.date.setText(current.getDate());
        holder.time.setText(current.getTime());
        holder.venue.setText(current.getVenue());
        holder.description.setText(current.getDescription());


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

    class ViewHolderEvents extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title,date,venue,time,description;
        public ViewHolderEvents(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.tvEventTitle);
            date = (TextView) view.findViewById(R.id.tvEventDate);
            venue = (TextView) view.findViewById(R.id.tvEventVenue);
            time = (TextView) view.findViewById(R.id.tvEventTime);
            description = (TextView) view.findViewById(R.id.tvEventDescription);

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
       public  void itemClicked(View view,int position);
    }
}
