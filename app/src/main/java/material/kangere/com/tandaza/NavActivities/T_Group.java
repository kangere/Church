package material.kangere.com.tandaza.NavActivities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import material.kangere.com.tandaza.NavigationFragment;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.videoimageupload.UploadActivity;


public class T_Group extends AppCompatActivity {

    private static final String TAG = T_Group.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private static final int FILE_SELECT_CODE = 0;
    private String Path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_group);

        //Toolbar init
        toolbar = (Toolbar) findViewById(R.id.connectToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("T-Group");
        toolbar.setTitleTextColor(Color.WHITE);

        //drawer layout init
        drawerLayout = (DrawerLayout) findViewById(R.id.about_drawer_layout);


        //navigation fragment init
        NavigationFragment navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.connect_fragment_navigation_drawer);
        navigationFragment.setup(drawerLayout, toolbar);
    }




    public void ShowFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select txt file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Uri selectedFile = data.getData();
            String[] filePath = {MediaStore.Files.FileColumns.DATA};
            Cursor c = getContentResolver().query(selectedFile, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            Path = c.getString(columnIndex);
            c.close();

            Log.d(TAG, Path);
            Toast.makeText(T_Group.this, Path, Toast.LENGTH_LONG).show();


        }
    }
    private void launchUploadActivity() {
        //sends the android file path to the Upload Activity
        Intent i = new Intent(T_Group.this, UploadActivity.class);
        i.putExtra("Path", Path);

        startActivity(i);

    }
}
