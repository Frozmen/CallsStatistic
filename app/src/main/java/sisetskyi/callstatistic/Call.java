package sisetskyi.callstatistic;

import java.util.Date;

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

    public Call(String contactName, String phNumber, CallType callType, Date callDayTime, String callDuration) {
        this.contactName = contactName;
        this.phNumber = phNumber;
        this.callType = callType;
        this.callDayTime = callDayTime;
        this.callDuration = callDuration;
    }

    @Override
    public String toString() {
        return "Call: " + phNumber + ", " + callType + ", " + callDayTime + ", " + callDuration;
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

    public String getCallDuration() {
        return callDuration;
    }
}
