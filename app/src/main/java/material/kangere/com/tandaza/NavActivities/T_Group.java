package material.kangere.com.tandaza.NavActivities;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.videoimageupload.UploadActivity;


public class T_Group extends Fragment {

    private static final String TAG = T_Group.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private WebView webView;
    private static final int FILE_SELECT_CODE = 0;
    private String Path;
    private StorageReference mStorageRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.t_group);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Toolbar init
        /*toolbar = (Toolbar) findViewById(R.id.connectToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("T-Group");
        toolbar.setTitleTextColor(Color.WHITE);

        //drawer layout init
        drawerLayout = (DrawerLayout) findViewById(R.id.about_drawer_layout);


        //navigation fragment init
        NavigationFragment navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.connect_fragment_navigation_drawer);
        navigationFragment.setup(drawerLayout, toolbar);*/
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.t_group,container,false);

        webView = (WebView) view.findViewById(R.id.wvTgroup);

        webView.loadUrl("http://tandaza.org/connect/");
       // Button uploadContent = (Button)  view.findViewById(R.id.bTgroupUpload);

        /*uploadContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowFileChooser();
            }
        });*/


        return view;
    }



    public void ShowFileChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select txt file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            Uri selectedFile = data.getData();
            String[] filePath = {MediaStore.Files.FileColumns.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedFile, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            Path = c.getString(columnIndex);
            c.close();

            Log.d(TAG, Path);
            Toast.makeText(getActivity(), Path, Toast.LENGTH_LONG).show();


        }
    }
    private void launchUploadActivity() {
        //sends the android file path to the Upload Activity
        Intent i = new Intent(getActivity(), UploadActivity.class);
        i.putExtra("Path", Path);

        startActivity(i);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("T-Groups");
    }
}
