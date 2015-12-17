package material.kangere.com.tandaza.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import material.kangere.com.tandaza.R;

/**
 * Created by user on 7/1/2015.
 */
public class NavAdapter extends ArrayAdapter<String> {

    private Activity context;
    private int[] imageIcon;
    private String[] name;
    public NavAdapter(Activity c,int[] imageIcon,String[] name){
          super(c, R.layout.main_nav_bar,name);
        this.context = c;
        this.imageIcon = imageIcon;
        this.name = name;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.main_nav_bar, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvNav);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listicon);


        txtTitle.setText(name[position]);
        imageView.setImageResource(imageIcon[position]);



        return rowView;
    }


}
