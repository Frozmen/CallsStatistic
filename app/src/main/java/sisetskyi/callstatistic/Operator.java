package sisetskyi.callstatistic;

/**
 * Created by User on 04.10.2016.
 */

public class Operator {

    String key;
    String[] phoneCodes;

    public Operator(String key, String[] phoneCodes) {
        this.key = key;
        this.phoneCodes = phoneCodes;
    }

    public String getKey() {
        return key;
    }

    public String[] getPhoneCodes() {
        return phoneCodes;
    }
}
