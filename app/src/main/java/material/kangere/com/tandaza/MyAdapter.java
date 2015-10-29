package material.kangere.com.tandaza;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by user on 10/14/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolderNotifications> {


    private ArrayList<ItemData> notificationsList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private static final String TAG_NID = "nid";
    private Context mContext;
    private ClickListener clickListener;

    public MyAdapter(Context context) {

        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setNotificationsList(ArrayList<ItemData> notificationsList) {
        this.notificationsList = notificationsList;
        notifyItemRangeChanged(0, notificationsList.size());
    }

    @Override
    public ViewHolderNotifications onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.note_list_cell, viewGroup, false);
        ViewHolderNotifications viewHolder = new ViewHolderNotifications(view);


        return viewHolder;
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolderNotifications viewHolderNotifications, int position) {

        ItemData currentNote = notificationsList.get(position);



        Uri uri = Uri.parse(currentNote.getImagePath());


        Glide.with(mContext).load(uri).into(viewHolderNotifications.imgNotification);

        viewHolderNotifications.nid.setText(currentNote.getNid());
        viewHolderNotifications.title.setText(currentNote.getTitle());
        viewHolderNotifications.notificationTimeStamp.setText(currentNote.getTime_stamp());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    class ViewHolderNotifications extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nid;
        private TextView title;

        private TextView notificationTimeStamp;
        private ImageView imgNotification;
        CardView cv;

        public ViewHolderNotifications(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            nid = (TextView) itemView.findViewById(R.id.nid);
            title = (TextView) itemView.findViewById(R.id.title);
            notificationTimeStamp = (TextView) itemView.findViewById(R.id.notificationTimeStamp);
            imgNotification = (ImageView) itemView.findViewById(R.id.img_notification);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

           /* String noteId = nid.getText().toString();
            Intent i = new Intent(context, ViewNotification.class);
            i.putExtra(TAG_NID, noteId);
            context.startActivity(i);*/

            if(clickListener != null){
                clickListener.itemClicked(v,getAdapterPosition());
            }

        }
    }

    public interface ClickListener {
         void itemClicked(View view, int position);

    }
}
