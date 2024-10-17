package Banking_Service.function;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
    public static String date(){
        //find current date_time and change int String value
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String date=dtf.format(now);
        return date;
    }
}
