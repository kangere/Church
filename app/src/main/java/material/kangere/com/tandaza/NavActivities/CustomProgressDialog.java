package material.kangere.com.tandaza.NavActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.WindowManager;

import material.kangere.com.tandaza.R;

/**
 * Created by user on 20/05/2017.
 */

public class CustomProgressDialog extends ProgressDialog {



    public CustomProgressDialog(Context context,String TAG)
    {
        super(context);

        try {
            show();
        } catch (WindowManager.BadTokenException e) {
            Log.e(TAG,e.toString());
        }

        setCancelable(false);

        try {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch(NullPointerException e)
        {
            Log.e(TAG,e.toString());
        }

        setContentView(R.layout.progressdialog);

    }


}
