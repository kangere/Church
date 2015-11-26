package material.kangere.com.tandaza;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

/**
 * Created by user on 10/21/2015.
 */
public class ViewNotification extends AppCompatActivity {

    private TextView title,content,ministry,timeStamp;
    private ImageView sourceImage;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);

        title = (TextView) findViewById(R.id.tvDetailNoteTitle);
        content = (TextView) findViewById(R.id.tvDetailNoteContent);
        ministry = (TextView) findViewById(R.id.tvDetailMinistry);
        timeStamp = (TextView) findViewById(R.id.tvDetailTimeStamp);
        sourceImage = (ImageView) findViewById(R.id.ivDetailNoteImage);
        toolbar = (Toolbar) findViewById(R.id.tbviewNote);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        Intent i = getIntent();
        String titleDummy = i.getExtras().getString("title");
        String ministryString = i.getStringExtra("ministry");
        String contentString = i.getStringExtra("contents");
        String timeStampString = i.getStringExtra("created_at");
        String imgPath = i.getStringExtra("imgpath");

        Uri uri = Uri.parse(imgPath);

        ministry.setText(ministryString);
        content.setText(contentString);
        timeStamp.setText(timeStampString);
        title.setText(titleDummy);
       // Log.d("Title testing", titleDummy);
        try {
            Glide.with(this).load(uri).into(sourceImage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }
}
