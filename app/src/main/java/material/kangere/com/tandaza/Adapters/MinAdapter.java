package material.kangere.com.tandaza.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import material.kangere.com.tandaza.R;

/**
 * Created by user on 11/5/2015.
 */
public class MinAdapter extends ArrayAdapter {
    private String[] titles;
    private int[] images;
    private AppCompatActivity a;


    public MinAdapter(AppCompatActivity a,String[] titles,int[] images){
        super(a,R.layout.ministry_cardview,titles);
        this.a = a;
        this.titles = titles;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = a.getLayoutInflater();
        View view = inflater.inflate(R.layout.ministry_cardview, null, true);
        ImageView image = (ImageView) view.findViewById(R.id.ivMin);
        TextView text = (TextView) view.findViewById(R.id.tvMin);

        image.setImageResource(images[position]);
        text.setText(titles[position]);
        return view;
    }
}
