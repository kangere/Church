package material.kangere.com.tandaza;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import material.kangere.com.tandaza.NavActivities.Show_Notifications;
import material.kangere.com.tandaza.util.ApiFields;
import material.kangere.com.tandaza.util.AppConfig;
import material.kangere.com.tandaza.util.CheckNetwork;
import material.kangere.com.tandaza.util.Permissions;
import material.kangere.com.tandaza.util.RequestQueueSingleton;
import material.kangere.com.tandaza.videoimageupload.UploadActivity;

import static android.app.Activity.RESULT_OK;

public class MakeNotification extends Fragment implements View.OnClickListener {

    // JSON Node names
    private static final String TAG = MakeNotification.class.getSimpleName();
    public String picturePath;
    private EditText title, content;
    private Spinner ministries;
    private Button upload, pick;

    public String file_path;
    private String y_title, y_content, n_ministries;
    private static final int PICK_FROM_GALLERY = 2;
    ImageView picView;


    final int PIC_CROP = 2;

    private ProgressDialog dialog;

    private final int NOTIFICATION_PROGRESS_DELAY = 1000;


//    JSONParser jsonParser = new JSONParser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_make_notification, container, false);

        upload = view.findViewById(R.id.bNoteUpload);
        pick = view.findViewById(R.id.bPick);
        title = view.findViewById(R.id.etYouthTitle);
        content = view.findViewById(R.id.etYouthContent);
        ministries = view.findViewById(R.id.sMinistries);
        picView = view.findViewById(R.id.imgPreview);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.ministries,
                R.layout.myspinner);
        ministries.setAdapter(adapter);

        pick.setOnClickListener(this);
        upload.setOnClickListener(this);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        } catch (NullPointerException e) {
            Log.d(TAG, " " + e);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bNoteUpload:

                if (fieldsAreEmpty())
                    Toast.makeText(getActivity(), "One or More Fields is Empty", Toast.LENGTH_LONG).show();
                else
                    postData();/*new UploadNote().execute();*/

                break;

            case R.id.bPick:

                Permissions.verifyStoragePermissions(getActivity());
                try {

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            PICK_FROM_GALLERY);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Phone does not support camera", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private boolean fieldsAreEmpty() {
        if (title.getText().toString().isEmpty() ||
                content.getText().toString().isEmpty())
            return true;

        return false;
    }

    /**
     * Method performs the network operations for uploading of the news content to the server
     * It implements Volley using StringRequest to upload the content
     */
    private void postData() {
        y_title = title.getText().toString();
        y_content = content.getText().toString();
        n_ministries = ministries.getSelectedItem().toString();

        //check if connected to internet first
        if (CheckNetwork.isInternetAvailable(getActivity())) {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Posting notification..");
            dialog.show();

            //Create request to post data
            StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);


                            int success = 0;


                            try {
                                JSONObject temp = new JSONObject(response);
                                success = temp.getInt(ApiFields.TAG_SUCCESS);

                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }

                            if (success == 1) {


                                Toast.makeText(getActivity(), "Notification created successfully", Toast.LENGTH_LONG).show();

                                //go to previous fragment
                                Show_Notifications showNotifications = new Show_Notifications();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                // Replace whatever is in the fragment_container view with this fragment,
                                // and add the transaction to the back stack
                                transaction.replace(R.id.flContent, showNotifications);
                                transaction.addToBackStack(null);

                                // Commit the transaction
                                transaction.commit();

                            } else {

                                Toast.makeText(getActivity(), "Failed to create notification, Try Again", Toast.LENGTH_LONG).show();

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.getMessage());
                        }
                    }
            ) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Building Parameters
                    Map<String, String> params = new HashMap<>();
                    params.put("title", y_title);

                    params.put("content", y_content);
                    params.put("ministry", n_ministries);

                    params.put("imgpath", file_path);

                    return params;
                }
            };

            RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    dialog.dismiss();
                }
            }, NOTIFICATION_PROGRESS_DELAY);
        } else {

            Toast.makeText(getActivity(),"You are not connected to the internet",Toast.LENGTH_LONG).show();

        }
    }


    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_FROM_GALLERY) {


                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                picView.setVisibility(View.VISIBLE);
                picView.setImageBitmap(thumbnail);
                Log.d(TAG, picturePath);
                Toast.makeText(getActivity(), picturePath, Toast.LENGTH_LONG).show();
                launchUploadActivity();


            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                picView.setVisibility(View.VISIBLE);
                picView = (ImageView) getActivity().findViewById(R.id.imgPreview);
                //display the returned cropped image
                picView.setImageBitmap(thePic);
            } else if (requestCode == 2404) {

                //Retrives the image server file path from Upload Activity using the request code.
                file_path = data.getStringExtra("file_path");
                Toast.makeText(getActivity(), !(file_path.isEmpty()) ? "Image Upload Success \n" + file_path : "Image Upload Failed \n" + file_path
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

    private void launchUploadActivity() {
        //sends the android file path to the Upload Activity
        Intent i = new Intent(getActivity(), UploadActivity.class);
        i.putExtra("filePath", picturePath);

        startActivityForResult(i, 2404);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:

                //go to previous fragment
                Show_Notifications showNotifications = new Show_Notifications();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.flContent, showNotifications);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

                break;
        }
        return true;
    }

    /*class UploadNote extends AsyncTask<String, String, String> {

        *//**
     * Before starting background thread Show Progress Dialog
     *//*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {
                pDialog = new CustomProgressDialog(getActivity(),TAG);
                pDialog.show();
            } else {
                pDialog.show();
            }
        }


        *//**
     * Creating product
     *//*
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

                    //finish activity
                    // finish();
                    //go to previous fragment
                    Show_Notifications showNotifications = new Show_Notifications();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.flContent, showNotifications);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();

                } else {
                    Log.e("Error", "Json request failed");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Something Seems to be wrong, Try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        */

    /**
     * After completing background task Dismiss the progress dialog
     * *
     *//*


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }*/
    /*public  void getText()
    {
        y_title = title.getText().toString();
        y_content = content.getText().toString();
        n_ministries = ministries.getSelectedItem().toString();

    }*/

}
