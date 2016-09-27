package sisetskyi.callstatistic;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.List;

/**
 * Created by User on 23.09.2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "callsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "calls";

    public static final String CONTACT_NAME_COLUMN = "cat_name";
    public static final String PHONE_NUMBER_COLUMN = "phone_number";
    public static final String CALL_DAY_TIME_COLUMN = "call_day_time";
    public static final String CALL_DURATION_COLUMN = "call_duration";
    public static final String CALL_TYPE_COLUMN = "call_type";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + CONTACT_NAME_COLUMN
            + CALL_DURATION_COLUMN + " text, " + CALL_TYPE_COLUMN + " text, "
            + " text, " + PHONE_NUMBER_COLUMN + " text, " + CALL_DAY_TIME_COLUMN
            + " integer);";

    private Context mContext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.mContext = context;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT);
        upgradeDataBase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void upgradeDataBase(){
        List<Call> calls = CallsStatisticProvider.getCallDetails(mContext);
        addListToDataBase(calls);
    }

    private void addListToDataBase(List<Call> calls){
        for(Call call : calls){
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.CONTACT_NAME_COLUMN, call.getContactName());
            values.put(DatabaseHelper.PHONE_NUMBER_COLUMN, call.getPhNumber());
            values.put(DatabaseHelper.CALL_DAY_TIME_COLUMN, call.getCallDayTimeInMillisecond());
            values.put(DatabaseHelper.CALL_DURATION_COLUMN, call.getCallDuration());
            values.put(DatabaseHelper.CALL_TYPE_COLUMN, call.getCallType().toString());
            getWritableDatabase().insert(DATABASE_TABLE, null, values);
        }
    }
}
