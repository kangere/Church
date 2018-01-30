package material.kangere.com.tandaza.videoimageupload;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import material.kangere.com.tandaza.MakeNotification;
import material.kangere.com.tandaza.R;
import material.kangere.com.tandaza.util.AppConfig;


public class UploadActivity extends Activity {
    // LogCat tag
    private static final String TAG = MakeNotification.class.getSimpleName();



   private String filePath = "";
    private ProgressBar progressBar;
    private TextView txtPercentage;
    private ImageView imgPreview;
    public String file_path;
    long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);

        progressBar = findViewById(R.id.pbUploadImage);
        txtPercentage =  findViewById(R.id.txtPercentage);
        imgPreview =  findViewById(R.id.imgPreview);
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");



        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {

            previewMedia(isImage);
            new UploadFileToServer().execute();
            Toast.makeText(UploadActivity.this, "Its working", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }




    }

    /**
     * Displaying captured image/video on the screen
     */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {

            //vidPreview.setVisibility(View.GONE);
            imgPreview.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {

            Toast.makeText(UploadActivity.this, "not working", Toast.LENGTH_LONG).show();

        }
    }




    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(values[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(values[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }


        private String uploadFile() {
            String responseString ;

//            Optional<URL> url = Optional.empty();
//            try {
//                url.get() = new URL(AppConfig.FILE_UPLOAD_URL);
//            }catch(MalformedURLException e){
//                Log.e(TAG,e.toString());
//            }
//
//            HttpURLConnection urlConnection = (HttpURLConnection) url.get().openConnection();
            //HttpURLConnection urlConnection =
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfig.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);


                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                //entity.addPart("image", new FileBody(sourceFile2));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }


            } catch (Exception e) {
                responseString = e.toString();
            }


            return responseString;

        }


        @Override
        protected void onPostExecute(String result) {

            //your response
            try {
                JSONObject json = new JSONObject(result);
                file_path = json.getString("file_path");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.d(TAG, "file_path " + file_path);

           showAlert(!(file_path.isEmpty())? "Image Upload Successful\n" + file_path : "Image Upload Failed\n" + file_path);

            //send back the server file path to AddNotification Activity using setResult method
            Intent intent= new Intent();
            intent.putExtra("file_path", file_path);
            setResult(RESULT_OK, intent);

        }


    }


    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
