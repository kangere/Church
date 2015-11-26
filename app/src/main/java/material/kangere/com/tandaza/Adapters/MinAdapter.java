package material.kangere.com.tandaza.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import material.kangere.com.tandaza.R;

/**
 * Created by user on 11/5/2015.
 */
public class MinAdapter extends BaseAdapter {
    Context mContext;
    int[] icons = {R.drawable.ic_about,R.drawable.children_ministry,R.drawable.ic_about,R.drawable.kings_men};
    public MinAdapter(Context c){
        mContext = c;
    }
    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }


}
