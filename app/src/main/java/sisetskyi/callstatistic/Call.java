package sisetskyi.callstatistic;

import java.util.Date;

/**
 * Created by User on 22.09.2016.
 */

public class Call {
    private String phNumber;
    private String callType;
    private Date callDayTime;
    private String callDuration;

    public Call(String phNumber, String callType, Date callDayTime, String callDuration) {
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
                    && callDayTime.equals(call.callDayTime) && callDuration.equals(call.callDuration)){
                return true;
            }
        }
        return false;
    }
}
