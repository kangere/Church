package material.kangere.com.tandaza.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import material.kangere.com.tandaza.R;

/**
 * Created by user on 12/28/2015.
 */
public class YouthAdapter extends ArrayAdapter<String> {

    AppCompatActivity context;
    String[] titles;
    String[] bodyText;
    public YouthAdapter(AppCompatActivity context,String[] titles,String[] bodyText) {
        super(context, R.layout.youth_card,titles);
        this.context = context;
        this.bodyText = bodyText;
        this.titles = titles;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.youth_card, null, true);

        TextView title = (TextView) rowView.findViewById(R.id.tvYouthHeader);
        TextView body = (TextView) rowView.findViewById(R.id.tvYouthText);

        title.setText(titles[position]);
        body.setText(bodyText[position]);
        return rowView;
    }
}

