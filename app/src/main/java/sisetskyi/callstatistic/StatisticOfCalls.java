package sisetskyi.callstatistic;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 04.10.2016.
 */

public class StatisticOfCalls {
    public static final String TAG = StatisticOfCalls.class.getSimpleName();
    private List<Call> calls;
    private Map<String, Long> operatorsStatisticInSec;

    public StatisticOfCalls(List<Call> callsList) {
        this.calls = callsList;
    }


    public Map<String, Long> getOperatorsStatisticInSec() {
        long startTime = System.currentTimeMillis();
        if (operatorsStatisticInSec == null) {
            countOperatorsStatisticInSec();
        }
        for (Operator operator : MobileOperatorsHelper.operators){
            Log.d(TAG, "getOperatorsStatisticInSec: Operator " + operator.getKey() + ", duration " + operatorsStatisticInSec.get(operator.getKey()));
        }
        long finishTime = System.currentTimeMillis();
        Log.d(TAG, "getOperatorsStatisticInSec: spent time - " + String.valueOf(finishTime - startTime));
        return operatorsStatisticInSec;
    }

    private void countOperatorsStatisticInSec() {
        Map<String, Integer> indexOfOperators = new HashMap<>();
        List<Operator> availableOperators = MobileOperatorsHelper.operators;
        int index = 0;
        for (Operator operator : availableOperators) {
            indexOfOperators.put(operator.getKey(), index);
            index++;
        }
        long[] durationCallsByOperators = new long[availableOperators.size()];
        for (Call currCall : calls) {
                String operatorKey = currCall.getOperatorConditionalCode();
                String durationOfCurrentCall = currCall.getCallDuration();
                int arrayIndex = indexOfOperators.get(operatorKey);
                durationCallsByOperators[arrayIndex] += Integer.valueOf(durationOfCurrentCall);
        }
        
        operatorsStatisticInSec = new HashMap<>();
        int i = 0;
        for (Operator operator : availableOperators) {
            operatorsStatisticInSec.put(operator.getKey(), durationCallsByOperators[i]);
            i++;
        }
    }
}
