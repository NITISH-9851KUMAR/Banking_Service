package Banking_Service.function;
import java.util.concurrent.ThreadLocalRandom;
public class AccNumber {
    public static String accNumber(){
        String ac_number1="447318210011";
        int ranThreeNumber= ThreadLocalRandom.current().nextInt(100,1000);
        String acc_number=ac_number1+ranThreeNumber;
        return acc_number;
    }

}
