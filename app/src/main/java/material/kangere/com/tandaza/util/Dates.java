package material.kangere.com.tandaza.util;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Dates {

    private static final String TAG = Dates.class.getSimpleName();

    public static String getTimeSpan(String time){

        Date parsedDate = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            parsedDate = dateFormat.parse(time);
        } catch (ParseException e) {//this generic but you can control another types of exception
            Log.e(TAG,e.toString());
        }

        return String.valueOf(DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS));
    }
}
