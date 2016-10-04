package sisetskyi.callstatistic;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04.10.2016.
 */

public class MobileOperatorsHelper {

    public static final String TAG = MobileOperatorsHelper.class.getSimpleName();
    public static final int MIN_LENGTH_OF_PHONE_NUMBER = 10;

    public static final String LIFECELL_CONDITIONAL_KEY = "lifecell";
    public static final String KYIVSTAR_CONDITIONAL_KEY = "kyivstar";
    public static final String MTS_CONDITIONAL_KEY = "mts";
    public static final String UTEL_CONDITIONAL_KEY = "utel";
    public static final String PEOPLENET_CONDITIONAL_KEY = "peoplenet";
    public static final String INTERTELECOM_CONDITIONAL_KEY = "intertelecom";
    public static final String UKRAINELOCAL_CONDITIONAL_KEY = "ukraine_local";
    public static final String UNKNOWN_CONDITIONAL_KEY = "unknown";

    private static String[] lifeCellCodes = new String[]{"073", "093", "063"};
    private static String[] kyivstarCodes = new String[]{"039", "067", "068", "096", "097", "098"};
    private static String[] mtsCodes = new String[]{"050", "066", "095", "099"};
    private static String[] utelCodes = new String[]{"091"};
    private static String[] peoplenetCodes = new String[]{"092"};
    private static String[] intertelecomCodes = new String[]{"094"};
    private static String[] ukraineLocalCodes = new String[]{"044"};

    private static Operator lifeCell = new Operator(LIFECELL_CONDITIONAL_KEY, lifeCellCodes);
    private static Operator kyivstar = new Operator(KYIVSTAR_CONDITIONAL_KEY, kyivstarCodes);
    private static Operator mts = new Operator(MTS_CONDITIONAL_KEY, mtsCodes);
    private static Operator utel = new Operator(UTEL_CONDITIONAL_KEY, utelCodes);
    private static Operator peoplenet = new Operator(PEOPLENET_CONDITIONAL_KEY, peoplenetCodes);
    private static Operator intertelecom = new Operator(INTERTELECOM_CONDITIONAL_KEY, intertelecomCodes);
    private static Operator ukraineLocal = new Operator(UKRAINELOCAL_CONDITIONAL_KEY, ukraineLocalCodes);

    public static List<Operator> operators = new ArrayList<>();

    static {
        operators.add(lifeCell);
        operators.add(kyivstar);
        operators.add(mts);
        operators.add(utel);
        operators.add(peoplenet);
        operators.add(intertelecom);
        operators.add(ukraineLocal);
    }

    public static String getOperatorConditionalCode(String phoneNumber) throws IllegalFormatNumberException {
        String phoneNumberCode = getCodeOfOperatorByNumber(phoneNumber);
        for(Operator currOperator : operators){
            String[] operatorsCodes = currOperator.getPhoneCodes();
            for(int i = 0; i < operatorsCodes.length; i++){
                if(phoneNumberCode.equals(operatorsCodes[i])){
                    return currOperator.getKey();
                }
            }
        }
        throw new IllegalFormatNumberException(IllegalFormatNumberException.NOT_FOUND_CODE);
    }

    public static String getOperatorNameByConditionalKey(String conditionalKey, Context context){
        switch (conditionalKey){
            case LIFECELL_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.lifeCell_name);
            case KYIVSTAR_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.kievstar_name);
            case MTS_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.mts_name);
            case UTEL_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.utel_name);
            case PEOPLENET_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.peoplenet_name);
            case INTERTELECOM_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.intertelecom_name);
            case UKRAINELOCAL_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.ukrainelocal_name);
            case UNKNOWN_CONDITIONAL_KEY:
                return context.getResources().getString(R.string.unknown);
        }
        return null;
    }

    private static String getCodeOfOperatorByNumber(String phoneNumber) throws IllegalFormatNumberException {
        checkException(phoneNumber);
        int indexOfFirstZero = phoneNumber.indexOf("0");
        return phoneNumber.substring(indexOfFirstZero, indexOfFirstZero+3);
    }

    private static void checkException(String phoneNumber) throws IllegalFormatNumberException {
        if(phoneNumber.length() < MIN_LENGTH_OF_PHONE_NUMBER){
            throw new IllegalFormatNumberException(IllegalFormatNumberException.ILLEGAL_PHONE_NUMBER_FORMAT +
                    " length of number must be more then " + MIN_LENGTH_OF_PHONE_NUMBER);
        }
        if(!(phoneNumber.startsWith("+380") || phoneNumber.startsWith("380") ||
                phoneNumber.startsWith("80") || phoneNumber.startsWith("0"))){
            throw new IllegalFormatNumberException(IllegalFormatNumberException.ILLEGAL_PHONE_NUMBER_FORMAT);
        }
    }


    public static class IllegalFormatNumberException extends Exception {
        public static final String ILLEGAL_PHONE_NUMBER_FORMAT = "ILLEGAL PHONE NUMBER";
        public static final String NOT_FOUND_CODE = "NO SUCH PHONE CODE";

        public IllegalFormatNumberException(String message) {
            super(message);
        }
    }
}
