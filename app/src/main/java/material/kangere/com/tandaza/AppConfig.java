package material.kangere.com.tandaza;

/**
 * Created by user on 10/7/2015.
 */
public class AppConfig {


   public static String URL_GET_ALL_NOTIFICATIONS = "http://www.kisilokitaka.com/tandaza/get_all_notifications.php";


    // Server user register url
    public static String URL_DELETE = "http://www.kisilokitaka.com/tandaza/delete_note.php";
    public static String URL_UPLOAD = "http://www.kisilokitaka.com/tandaza/upload_note.php";
    public static String FILE_UPLOAD_URL = "http://www.kisilokitaka.com/tandaza/file_upload.php";
    public static String EVENT_UPLOAD_URL = "http://www.kisilokitaka.com/tandaza/upload_event.php";
    public static String URL_GET_EVENTS = "http://www.kisilokitaka.com/tandaza/get_events.php";

    public static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int REQUEST_EXTERNAL_STORAGE = 1;

    //Local server
    //For testing purposes
   /* public static String URL_GET_ALL_NOTIFICATIONS = "http://192.168.0.25/tandaza_api/get_all_notifications.php";


    // Server user register url
    public static String URL_UPLOAD = "http://192.168.0.25/tandaza_api/upload_note.php";
    public static String FILE_UPLOAD_URL = "http://192.168.0.25/tandaza_api/file_upload.php";
    public static String EVENT_UPLOAD_URL = "http://192.168.0.25/tandaza_api/upload_event.php";
    public static String URL_GET_EVENTS = "http://192.168.0.25/tandaza_api/get_events.php";*/


}
