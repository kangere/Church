package material.kangere.com.tandaza.NavActivities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import material.kangere.com.tandaza.Adapters.YouthAdapter;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.StaticMethods;

public class YouthMinistry extends AppCompatActivity {


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youth_ministry);

        //toolbar initisialization
        StaticMethods.toolBar(this,R.id.youthToolbar);

        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        //data for listview
        String[] titles = getResources().getStringArray(R.array.youth_headers);
        String[] body = getResources().getStringArray(R.array.youth_text);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.youth_header,null);


        listView = (ListView) findViewById(R.id.lvYouth);
        listView.addHeaderView(view);

        YouthAdapter youthAdapter = new YouthAdapter(this,titles,body);
        listView.setAdapter(youthAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_youth_ministry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
