package sisetskyi.callstatistic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 22.09.2016.
 */

public class Call {
    public enum CallType {
        OUTGOING, INCOMING, MISSED
    }
    private String contactName;
    private String phNumber;
    private CallType callType;
    private Date callDayTime;
    private String callDuration;
    private String operatorConditionalCode;

    public Call(String contactName, String phNumber, CallType callType, Date callDayTime, String callDuration) {
        this.contactName = contactName;
        this.phNumber = phNumber;
        this.callType = callType;
        this.callDayTime = callDayTime;
        this.callDuration = callDuration;
        setOperatorConditionalKey();
    }

    public Call(String contactName, String phNumber, CallType callType, Date callDayTime, String callDuration, String operatorConditionalCode) {
        this.contactName = contactName;
        this.phNumber = phNumber;
        this.callType = callType;
        this.callDayTime = callDayTime;
        this.callDuration = callDuration;
        this.operatorConditionalCode = operatorConditionalCode;
    }

    @Override
    public String toString() {
        return "Call: " + phNumber + ", " + operatorConditionalCode + ", " + callType + ", " + callDayTime + ", " + callDuration;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Call){
            Call call = (Call) obj;
            if(phNumber.equals(call.phNumber) && callType.equals(call.callType)
                    && callDayTime.equals(call.callDayTime) && callDuration.equals(call.callDuration)
                    && contactName.equals(call.contactName)){
                return true;
            }
        }
        return false;
    }

    private void setOperatorConditionalKey(){
        try {
            operatorConditionalCode = MobileOperatorsHelper.getOperatorConditionalCode(phNumber);
        } catch (MobileOperatorsHelper.IllegalFormatNumberException e) {
            operatorConditionalCode = MobileOperatorsHelper.UNKNOWN_CONDITIONAL_KEY;
            e.printStackTrace();
        }
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public CallType getCallType() {
        return callType;
    }

    public Date getCallDayTime() {
        return callDayTime;
    }

    public long getCallDayTimeInMillisecond() {
        return callDayTime.getTime();
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(callDayTime);
    }

    public String getCallDuration() {
        return callDuration;
    }

    public String getOperatorConditionalCode() {
        return operatorConditionalCode;
    }
}
