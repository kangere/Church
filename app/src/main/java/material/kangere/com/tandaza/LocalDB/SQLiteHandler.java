package material.kangere.com.tandaza.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import material.kangere.com.tandaza.LocalDB.TablesContract.EventsEntry;
import material.kangere.com.tandaza.LocalDB.TablesContract.NotificationsCache;
import material.kangere.com.tandaza.LocalDB.TablesContract.UserEntry;

/**
 * Created by user on 10/7/2015.
 */
@Deprecated
public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LNAME = "l_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_LNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        String CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.USERTABLE + "("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + UserEntry.COLUMN_EMAIL + " TEXT UNIQUE,"
                +UserEntry.COLUMN_NAME + " TEXT," + UserEntry.COLUMN_PASSWORD + " TEXT," + UserEntry.COLUMN_IS_STAFF
                +" INTEGER" +");";
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + EventsEntry.TABLE_NAME + "("
                + EventsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EventsEntry.COLUMN_EVENT_CACHE
                + " TEXT" + ");";
        String CREATE_NOTIFICATION_CACHE_TABLE = "CREATE TABLE " + NotificationsCache.TABLE_NAME + " ("
                + NotificationsCache._ID + " INTEGER PRIMARY KEY," + NotificationsCache.COLUMN_NOTE_CACHE
                + " TEXT" + ");";
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_NOTIFICATION_CACHE_TABLE);
        Log.d(TAG, "Database tables created");


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }


    /**
    * Method adds table and inserts a null value if it doesn't exist, if it table already
    * exists it does nothing
    *
    * @author - Paul Kang'ere
    * @param tablename  String: the tablename to be added
    * @param column  String: the column name to add null value
     */
    public void addTable(String tablename,String column){
        SQLiteDatabase db = this.getWritableDatabase();

        String countQuery = "SELECT  * FROM " + tablename;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        Log.d( tablename + " DB Rows","" + cnt);

        //check if table exists
        Cursor c = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tablename+"'", null);

        //if table doesn't exist add a null value to create it
        if(cnt <= 0)
        {
            ContentValues values = new ContentValues();
            //values.put(KEY_CACHE_ID, cache_id); // Name
            values.putNull(column); // StudentId
            //values.put(KEY_NOTIFICATION_CACHE, notification_cache); //Email

            long id = db.insert(tablename, null, values);


            Log.d(TAG, "New " + tablename + " inserted into sqlite: " + id);
        }else {
            Log.d(TAG,tablename + " table already exists");
        }
        c.close();
        cursor.close();
        db.close(); // Closing database connection
    }



    public void updateNotificationCache(String note_cache)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(NotificationsCache.COLUMN_NOTE_CACHE, note_cache);

        db.update(NotificationsCache.TABLE_NAME, args, NotificationsCache._ID + "= '" + 1 + "'", null);
        //db.close();


    }
    public void updateEventsCache(String note_cache)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(EventsEntry.COLUMN_EVENT_CACHE , note_cache);

        db.update(EventsEntry.TABLE_NAME, args, EventsEntry._ID + "= '" + 1 + "'", null);
        //db.close();


    }
    public HashMap<String, String> getEventCacheDetails() {
        HashMap<String, String> eventCache = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + EventsEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            //cache.put("cache_id", cursor.getString(1));
            eventCache.put("event_cache", cursor.getString(1));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching event from Sqlite: " + eventCache.toString());

        return eventCache;
    }

    public HashMap<String, String> getNotificationCache() {
        HashMap<String, String> noteCache = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + NotificationsCache.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            //cache.put("cache_id", cursor.getString(1));
            noteCache.put("notification_cache", cursor.getString(1));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching notification from Sqlite: " + noteCache.toString());

        return noteCache;
    }
}
