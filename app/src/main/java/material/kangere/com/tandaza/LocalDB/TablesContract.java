package material.kangere.com.tandaza.LocalDB;

import android.provider.BaseColumns;

/**
 * Created by user on 12/14/2015.
 */
public class TablesContract  {

    public TablesContract()
    {

    }

    public static abstract class UserEntry implements BaseColumns
    {
        public static final String USERTABLE = "users";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_IS_STAFF = "isStaff";
    }
    public static abstract class EventsEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_VENUE = "venue";
        public static final String COLUMN_DESCRIPTION = "description";
    }

}
