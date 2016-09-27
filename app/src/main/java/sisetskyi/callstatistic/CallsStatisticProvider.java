package sisetskyi.callstatistic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 22.09.2016.
 */

public class CallsStatisticProvider {

    private static final String TAG = CallsStatisticProvider.class.getName();
    private Context context;
    private List<Call> calls;

    public CallsStatisticProvider(Context context) {
        this.context = context;
        updateStatistic();
    }

    public static List<Call> getCallDetails(Context context) {
        List<Call> callList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        int contactNameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        while (cursor.moveToNext()) {
            String contactName = cursor.getString(contactNameIndex);
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            Call.CallType dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = Call.CallType.OUTGOING;
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = Call.CallType.INCOMING;;
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = Call.CallType.MISSED;
                    break;
            }
            callList.add(new Call(contactName, phNumber, dir, callDayTime, callDuration));
        }
        cursor.close();
        return callList;
    }

    public static List<Call> getCallsListAfterConcreteCall(Context context, Call call){
        List<Call> callList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String selection = CallLog.Calls.DATE + " > ?";
        String[] selectionArgs = new String[] {String.valueOf(call.getCallDayTimeInMillisecond())};

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, selection, selectionArgs, CallLog.Calls.DATE + " DESC");

        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        int contactNameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        while (cursor.moveToNext()) {
            String contactName = cursor.getString(contactNameIndex);
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            Call.CallType dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = Call.CallType.OUTGOING;
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = Call.CallType.INCOMING;;
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = Call.CallType.MISSED;
                    break;
            }
            callList.add(new Call(contactName, phNumber, dir, callDayTime, callDuration));
        }
        cursor.close();
        return callList;
    }



    public void updateStatistic(){
        List<Call> tempCalls = getCallDetails(context);
        if(calls == null){
            calls = tempCalls;
        } else {
            for(Call call : calls){
                if(!tempCalls.contains(call)){
                    Log.d(TAG, "CallsStatisticProvider: was deleted this call" + call.toString());
                }
            }
            Log.d(TAG, "updateStatistic: nothing delet");
            calls = tempCalls;

        }
        Log.d(TAG, "CallsStatisticProvider: " + calls.size());
        Log.d(TAG, "CallsStatisticProvider: " + calls.get(calls.size()-1).toString());
        Log.d(TAG, "CallsStatisticProvider: " + calls.get(0).toString());
    }
}
