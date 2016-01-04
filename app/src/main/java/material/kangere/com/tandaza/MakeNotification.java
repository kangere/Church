package material.kangere.com.tandaza;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import material.kangere.com.tandaza.NavActivities.Show_Notifications;
import material.kangere.com.tandaza.videoimageupload.UploadActivity;

public class MakeNotification extends AppCompatActivity {

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static  final  String TAG = MakeNotification.class.getSimpleName();
    private ProgressDialog pDialog;
    public String picturePath;
    private EditText title,content;
    private Spinner ministries;
    private Button upload,pick;

    public String file_path;
    private String y_title,y_content,n_ministries ;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    ImageView picView;


    final int PIC_CROP = 2;

    private Uri fileUri; // file url to store image/video



    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_make_notification);


        //Toolbar init
        StaticMethods.ClassInitisialisation(this, R.id.Make_Note_fragment_navigation_drawer, R.id.toolBarMakeNote, R.id.Make_Note_drawer_layout);


        upload  = (Button) findViewById(R.id.bNoteUpload);
        pick = (Button) findViewById(R.id.bPick);
        title = (EditText) findViewById(R.id.etYouthTitle);
        content = (EditText) findViewById(R.id.etYouthContent);
        ministries = (Spinner) findViewById(R.id.sMinistries);
        picView = (ImageView) findViewById(R.id.imgPreview);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                                            R.array.ministries,
                                                                            R.layout.myspinner);
        ministries.setAdapter(adapter);


        //file_path = "dummy path";


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // capture picture
                try {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException anfe) {
                    Toast.makeText(MakeNotification.this, "Phone does not support camera", Toast.LENGTH_LONG).show();

                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadNote().execute();
            }
        });
    }
    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_FROM_GALLERY) {


                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                picView.setVisibility(View.VISIBLE);
                picView.setImageBitmap(thumbnail);
                Log.d(TAG,picturePath);
                Toast.makeText(MakeNotification.this, picturePath, Toast.LENGTH_LONG).show();
                launchUploadActivity();


            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                picView.setVisibility(View.VISIBLE);
                picView = (ImageView) findViewById(R.id.imgPreview);
//display the returned cropped image
                picView.setImageBitmap(thePic);
            } else if (requestCode == 2404) {

                //Retrives the image server file path from Upload Activity using the request code.
                file_path = data.getStringExtra("file_path");
                Toast.makeText(MakeNotification.this, file_path, Toast.LENGTH_LONG).show();
            }
        }
    }
    private void launchUploadActivity() {
        //sends the android file path to the Upload Activity
        Intent i = new Intent(MakeNotification.this, UploadActivity.class);
        i.putExtra("filePath", picturePath);

        startActivityForResult(i, 2404);

    }
    class UploadNote extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {
                pDialog = Show_Notifications.createProgrssDialog(MakeNotification.this);
                pDialog.show();
            } else {
                pDialog.show();
            }
        }


        /**
         * Creating product
         */
        protected String doInBackground(String... args) {


            getText();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title", y_title));

            params.add(new BasicNameValuePair("content", y_content));
            params.add(new BasicNameValuePair("ministry", n_ministries));

            params.add(new BasicNameValuePair("imgpath", file_path));

            // getting JSON Object
            // Note that create notification url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_UPLOAD,
                    "POST", params);


            // check log cat fro response
            Log.d("Create Response", json.toString());


            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);


                if (success == 1) {

                    //onBackPressed();
                    finish();

                } else {
                    // failed to create product
                    //Toast.makeText(getApplicationContext(), "trial", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
    public  void getText()
    {
        y_title = title.getText().toString();
        y_content = content.getText().toString();
        n_ministries = ministries.getSelectedItem().toString();

    }

}
