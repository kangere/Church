package material.kangere.com.tandaza;

/**
 * Created by user on 10/7/2015.
 */
public class AppConfig {

    // Server user login url
   // public static String URL_LOGIN = "http://192.168.0.102/android_login_api/login.php";
    public static String URL_LOGIN = "http://192.168.0.105/tandaza_api/login.php";
    public static String URL_GET_ALL_NOTIFICATIONS = "http://192.168.0.105/tandaza_api/get_all_notifications.php";


    // Server user register url
   // public static String URL_REGISTER = "http://192.168.0.105/android_login_api/register.php";
    public static String URL_REGISTER = "http://192.168.0.105/tandaza_api/register.php";
    public static String URL_UPLOAD = "http://192.168.0.105/tandaza_api/upload_note.php";
    public static String FILE_UPLOAD_URL = "http://192.168.0.105/tandaza_api/file_upload.php";
    public static String EVENT_UPLOAD_URL = "http://192.168.0.105/tandaza_api/upload_event.php";
}
