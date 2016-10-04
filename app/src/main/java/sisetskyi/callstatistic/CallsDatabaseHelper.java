package sisetskyi.callstatistic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 23.09.2016.
 */

public class CallsDatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String TAG = CallsDatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "callsDatabase.db";
    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_TABLE = "calls";

    public static final String CONTACT_NAME_COLUMN = "cat_name";
    public static final String PHONE_NUMBER_COLUMN = "phone_number";
    public static final String CALL_DAY_TIME_COLUMN = "call_day_time";
    public static final String CALL_DURATION_COLUMN = "call_duration";
    public static final String CALL_TYPE_COLUMN = "call_type";
    public static final String PHONE_NUMBER_OPERATOR_COLUMN = "phone_number_operator";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID  + " integer primary key autoincrement, "
            + CONTACT_NAME_COLUMN + " text, " + CALL_DURATION_COLUMN + " text, "
            + CALL_TYPE_COLUMN + " text, "  + PHONE_NUMBER_COLUMN + " text, "
            + PHONE_NUMBER_OPERATOR_COLUMN + " text, " + CALL_DAY_TIME_COLUMN + " integer);";

    private Context mContext;

    public CallsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    public CallsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.mContext = context;
    }

    public CallsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT);
        Log.i(TAG, "createDataBase: data base created");
        createDataBase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        Log.i(TAG, "createDataBase: data base DROPPED");
        onCreate(sqLiteDatabase);
    }

    public void createDataBase(SQLiteDatabase sqLiteDatabase){
        List<Call> calls = CallsProvider.getAllCalls(mContext);
        addListToDataBase(sqLiteDatabase, calls);
    }

    public void updateCallsDataBase(){
        List<Call> calls = CallsProvider.getCallsListAfterConcreteCall(mContext, getLastCall());
        addListToDataBase(this.getWritableDatabase(), calls);
    }

    private Call getLastCall(){
        Cursor cursor = getReadableDatabase().query(DATABASE_TABLE, new String[] { "max(" + CALL_DAY_TIME_COLUMN + ")", "*" }, null, null,
                null, null, null);
        cursor.moveToFirst();

        int contactNameIndex = cursor.getColumnIndex(CONTACT_NAME_COLUMN);
        int number = cursor.getColumnIndex(PHONE_NUMBER_COLUMN);
        int type = cursor.getColumnIndex(CALL_TYPE_COLUMN);
        int date = cursor.getColumnIndex(CALL_DAY_TIME_COLUMN);
        int duration = cursor.getColumnIndex(CALL_DURATION_COLUMN);
        int operator = cursor.getColumnIndex(PHONE_NUMBER_OPERATOR_COLUMN);

        String callDate = cursor.getString(date);
        String contactName = cursor.getString(contactNameIndex);
        String phNumber = cursor.getString(number);
        String callType = cursor.getString(type);
        Date callDayTime = new Date(Long.valueOf(callDate));
        String callDuration = cursor.getString(duration);
        String operatorKey = cursor.getString(operator);

        Call lastCallInDB = new Call(contactName, phNumber, Call.CallType.valueOf(callType), callDayTime, callDuration, operatorKey);
        Log.d(TAG, "updateCallsDataBse: last call in db was " + lastCallInDB.toString());
        cursor.close();
        return lastCallInDB;
    }

    public List<Call> getAllCalls(){
        List<Call> callList = new ArrayList<>();

        Cursor cursor = getReadableDatabase().query(DATABASE_TABLE, null, null, null,
                null, null, null);
        int contactNameIndex = cursor.getColumnIndex(CONTACT_NAME_COLUMN);
        int number = cursor.getColumnIndex(PHONE_NUMBER_COLUMN);
        int type = cursor.getColumnIndex(CALL_TYPE_COLUMN);
        int date = cursor.getColumnIndex(CALL_DAY_TIME_COLUMN);
        int duration = cursor.getColumnIndex(CALL_DURATION_COLUMN);
        int operator = cursor.getColumnIndex(PHONE_NUMBER_OPERATOR_COLUMN);

        while (cursor.moveToNext()){
            String callDate = cursor.getString(date);
            String contactName = cursor.getString(contactNameIndex);
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String operatorKey = cursor.getString(operator);

            callList.add(new Call(contactName, phNumber, Call.CallType.valueOf(callType), callDayTime, callDuration, operatorKey));
        }
        cursor.close();
        return callList;
    }

    private void addListToDataBase(SQLiteDatabase db, List<Call> calls){
        for(Call call : calls){
            db.insert(DATABASE_TABLE, null, callToContentValues(call));
        }
        Log.i(TAG, "addListToDataBase: added " + calls.size() + " calls");
    }

    private ContentValues callToContentValues(Call call){
        ContentValues values = new ContentValues();
        values.put(CallsDatabaseHelper.CONTACT_NAME_COLUMN, call.getContactName());
        values.put(CallsDatabaseHelper.PHONE_NUMBER_COLUMN, call.getPhNumber());
        values.put(CallsDatabaseHelper.CALL_DAY_TIME_COLUMN, call.getCallDayTimeInMillisecond());
        values.put(CallsDatabaseHelper.CALL_DURATION_COLUMN, call.getCallDuration());
        values.put(CallsDatabaseHelper.CALL_TYPE_COLUMN, call.getCallType().toString());
        values.put(CallsDatabaseHelper.PHONE_NUMBER_OPERATOR_COLUMN, call.getOperatorConditionalCode());
        return values;
    }


}
