package material.kangere.com.tandaza.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by user on 22/08/2017.
 */

public class UploadService extends IntentService {

    private  final String TAG = UploadService.class.getSimpleName();
    private  final String TAG_SUCCESS = "success";

    public UploadService(){
        super("UploadService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String[] values = intent.getStringArrayExtra("note_values");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
