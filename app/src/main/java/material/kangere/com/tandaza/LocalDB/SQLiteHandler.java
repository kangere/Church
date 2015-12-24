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
     * Storing user details in database
     */
    public void addUser(String name,String l_name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_LNAME, l_name); // Last Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails(String email) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + UserEntry.USERTABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectionArgs = UserEntry.COLUMN_EMAIL +  " =?";
        Cursor cursor = db.query(UserEntry.USERTABLE,null,selectionArgs,new String[]{email},null,null,null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("email", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("isStaff", cursor.getString(4));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    public String getSingleUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String password;
        String selection = UserEntry.COLUMN_EMAIL +  " =?";
        Cursor cursor = db.query(TablesContract.UserEntry.USERTABLE,null,selection,new String[]{email},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            password = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASSWORD));
            cursor.close();

        }else {
            Log.d(TAG,"no user found: cursor returned " + cursor.getCount());
            password = "User does not exist";

        }
        db.close();
        return password;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void bulkInsert(){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String [] names = {"member","jane","john"};
        String[] emails = {"member","jane@gmail.com","john@gmail.com"};
        String[] passwords = {null,"jane123","john123"};
        int[] isStaff = {0,1,1};

        db.beginTransaction();
        try {
            for (int i = 0; i < names.length; ++i) {
                values.put(UserEntry.COLUMN_NAME, names[i]);
                values.put(UserEntry.COLUMN_EMAIL, emails[i]);
                values.put(UserEntry.COLUMN_PASSWORD, passwords[i]);
                values.put(UserEntry.COLUMN_IS_STAFF, isStaff[i]);
                 db.insert(UserEntry.USERTABLE, null, values);

            }

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

    }
    public void updateNotificationCache(String note_cache)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(NotificationsCache.COLUMN_NOTE_CACHE , note_cache);

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
}
