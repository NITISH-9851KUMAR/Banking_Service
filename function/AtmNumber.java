package Banking_Service.function;
import java.util.concurrent.ThreadLocalRandom;
public class AtmNumber {
    public static String atmNumber(){
        int atm_number1 = ThreadLocalRandom.current().nextInt(1000, 9999);
        int atm_number2 = ThreadLocalRandom.current().nextInt(1000, 9999);
        int atm_number3 = ThreadLocalRandom.current().nextInt(1000, 9999);
        int atm_number4 = ThreadLocalRandom.current().nextInt(1000, 9999);
        String atm_number = "" + atm_number1 + atm_number2 + atm_number3 + atm_number4;
        return atm_number;
    }
}
